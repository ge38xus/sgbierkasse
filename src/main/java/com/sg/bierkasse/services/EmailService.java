package com.sg.bierkasse.services;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.utils.EmailTemplates;
import com.sg.bierkasse.utils.helpers.FormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.sg.bierkasse.utils.helpers.FormatUtils.formatDateToDisplay;
import static com.sg.bierkasse.utils.helpers.FormatUtils.formatDoubleToEuro;

@Service
public class EmailService {
    private Session session = null;
    @Value("${spring.settings.emailservice.user}")
    private String user;
    @Value("${spring.settings.emailservice.password}")
    private String password;
    @Value("${spring.settings.payment.link}")
    private String link;
    @Value("${spring.settings.payment.iban}")
    private String iban;

    public EmailService() {
        initConnection();
    }

    public void initConnection() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",  "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }

    public void sendMail(PersonDTO personDTO, BillDTO billDTO, EmailTemplates emailTemplate) throws MessagingException, IOException {
        if (!personDTO.getEmail().isEmpty()) {
            Message message = prepareMessage(session, personDTO, billDTO, emailTemplate);
            Transport.send(message);
        }
    }

    public void sendMail(PersonDTO personDTO, EmailTemplates emailTemplate) throws MessagingException, IOException {
        this.sendMail(personDTO, null, emailTemplate);
    }

    private Message prepareMessage(
            Session session, PersonDTO personDTO, BillDTO billDTO, EmailTemplates emailTemplate) throws IOException, MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress("bierkasse.sg@gmail.com", "Bierkasse S-G!"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(personDTO.getEmail()));
        String subject = emailTemplate.subject.replace("${current-date}", FormatUtils.formatDateToDisplay(new Date()));
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart("related");

        BodyPart messageBodyPart = new MimeBodyPart();
        Path path = Path.of(emailTemplate.pathToFile);
        String someHtmlMessage = Files.readString(path);
        someHtmlMessage = replaceHtmlTemplateVariables(someHtmlMessage, personDTO, billDTO);
        messageBodyPart.setContent(someHtmlMessage, "text/html; charset=utf-8");
        multipart.addBodyPart(messageBodyPart);

        attachPicture(multipart, "sg-wappen", "src/main/resources/images/image-2.png");
        attachPicture(multipart, "paypal", "src/main/resources/images/image-1.png");

        if (emailTemplate == EmailTemplates.BERICHT) {
            attachPicture(multipart, "bierkassenbericht", "./Bierkassenbericht.pdf");
        }

        message.setContent(multipart);
        return message;
    }

    private void attachPicture(MimeMultipart multipart, String fileID, String pathToFile) throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource(pathToFile);
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", fileID);
        messageBodyPart.setFileName(pathToFile.substring(pathToFile.lastIndexOf('/') + 1));
        multipart.addBodyPart(messageBodyPart);
    }

    public Map<String, String> getMailTemplateVariables(PersonDTO personDTO, BillDTO billDTO) {
        Map<String, String> variables = getPersonMailTemplateVariables(personDTO);

        if (billDTO != null) {
            variables.put("red-count", String.valueOf(billDTO.red()));
            variables.put("blue-count", String.valueOf(billDTO.blue()));
            variables.put("white-count", String.valueOf(billDTO.white()));
            variables.put("green-count", String.valueOf(billDTO.green()));
        }

        variables.put("blue-price", formatDoubleToEuro(BillDTO.BLUE_VALUE));
        variables.put("red-price", formatDoubleToEuro(BillDTO.RED_VALUE));
        variables.put("white-price", formatDoubleToEuro(BillDTO.WHITE_VALUE));

        if (billDTO != null) {
            variables.put("green-price", formatDoubleToEuro(billDTO.greenValue()));

            variables.put("blue-subtotal", formatDoubleToEuro(billDTO.blue() * BillDTO.BLUE_VALUE));
            variables.put("red-subtotal", formatDoubleToEuro(billDTO.red() * BillDTO.RED_VALUE));
            variables.put("white-subtotal", formatDoubleToEuro(billDTO.white() * BillDTO.WHITE_VALUE));
            variables.put("green-subtotal", formatDoubleToEuro(billDTO.green() * billDTO.greenValue()));
            variables.put("sum", formatDoubleToEuro(Math.abs(billDTO.value())));
            variables.put("ring-green-text", billDTO.greenText());
        }

        variables.put("payment-link", link);
        variables.put("payment-iban", iban);
        return variables;
    }

    private static Map<String, String> getPersonMailTemplateVariables(PersonDTO personDTO) {
        Map<String, String> variables = new HashMap<>();
        variables.put("name", personDTO.getLastName());
        variables.put("current-date", formatDateToDisplay(new Date()));
        variables.put("user-balance", formatDoubleToEuro(personDTO.getBalance()));
        return variables;
    }

    public String replaceHtmlTemplateVariables(String someHtmlMessage, PersonDTO personDTO, BillDTO billDTO) {
        Map<String, String> variables = getMailTemplateVariables(personDTO, billDTO);

        for (String key : variables.keySet()) {
            String nextkey = "${" + key + "}";
            someHtmlMessage = someHtmlMessage.replace(nextkey, variables.get(key));
        }

        return someHtmlMessage;
    }
}

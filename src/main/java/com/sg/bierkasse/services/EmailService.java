package com.sg.bierkasse.services;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.utils.EmailTemplates;
import com.sg.bierkasse.utils.helpers.FormatUtils;
import com.sg.bierkasse.utils.helpers.PDFUtils;
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
import java.util.Properties;

@Service
public class EmailService {
    private static final String myAccount = "bierkasse.sg@gmail.com";
    private Session session = null;

    @Value("${spring.emailservice.password}")
    private String myPassword;

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
                return new PasswordAuthentication(myAccount, myPassword);
            }
        });
    }

    public void sendMail(PersonDTO personDTO, BillDTO billDTO, EmailTemplates emailTemplate) throws MessagingException, IOException {
        if (!personDTO.getEmail().isEmpty()) {
            Message message = prepareMessage(session, personDTO, billDTO, emailTemplate);
            Transport.send(message); // E-Mail senden!
        }
    }

    public void sendMail(PersonDTO personDTO, EmailTemplates emailTemplate) throws MessagingException, IOException {
        this.sendMail(personDTO, null, emailTemplate);
    }

    private static Message prepareMessage(
            Session session, PersonDTO personDTO, BillDTO billDTO, EmailTemplates emailTemplate) throws IOException, MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(myAccount, "Bierkasse S-G!"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(personDTO.getEmail()));
        String subject = emailTemplate.subject.replace("${current-date}", FormatUtils.formatDateToDisplay(new Date()));
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart("related");

        BodyPart messageBodyPart = new MimeBodyPart();
        Path path = Path.of(emailTemplate.pathToFile);
        String someHtmlMessage = Files.readString(path);
        someHtmlMessage = PDFUtils.replaceHtmlTemplateVariables(someHtmlMessage, personDTO, billDTO);
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

    private static void attachPicture(MimeMultipart multipart, String fileID, String pathToFile) throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource(pathToFile);
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", fileID);
        messageBodyPart.setFileName(pathToFile.substring(pathToFile.lastIndexOf('/') + 1));
        multipart.addBodyPart(messageBodyPart);
    }
}

package com.sg.bierkasse.utils.helpers;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.dtos.SpendeDTO;

import java.util.*;

import static com.sg.bierkasse.utils.helpers.FormatUtils.formatDateToDisplay;
import static com.sg.bierkasse.utils.helpers.FormatUtils.formatDoubleToEuro;

public class PDFUtils {
    public static Map<String, String> getMailTemplateVariables(PersonDTO personDTO, BillDTO billDTO) {
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

        return variables;
    }

    public static Map<String, String> getMailTemplateVariables(PersonDTO personDTO, SpendeDTO spendeDTO) {
        Map<String, String> variables = getPersonMailTemplateVariables(personDTO);

        if (spendeDTO != null) {
            variables.put("spende-date", String.valueOf(spendeDTO.date()));
            variables.put("spende-occasion", String.valueOf(spendeDTO.occasion()));
            variables.put("spende-value", String.valueOf(spendeDTO.value()));
        }

        return variables;
    }

    private static Map<String, String> getPersonMailTemplateVariables(PersonDTO personDTO) {
        Map<String, String> variables = new HashMap<>();
        variables.put("name", personDTO.getLastName());
        variables.put("current-date", formatDateToDisplay(new Date()));
        variables.put("user-balance", formatDoubleToEuro(personDTO.getBalance()));
        return variables;
    }

    public static String replaceHtmlTemplateVariables(String someHtmlMessage, PersonDTO personDTO, BillDTO billDTO) {
        Map<String, String> variables = getMailTemplateVariables(personDTO, billDTO);

        for (String key : variables.keySet()) {
            String nextkey = "${" + key + "}";
            someHtmlMessage = someHtmlMessage.replace(nextkey, variables.get(key));
        }

        return someHtmlMessage;
    }
}

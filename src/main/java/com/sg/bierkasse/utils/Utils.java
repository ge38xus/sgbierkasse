package com.sg.bierkasse.utils;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.vaadin.flow.component.combobox.ComboBox;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    private static final DecimalFormat df = new DecimalFormat("#.00");
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
    public static ComboBox<PersonRecord> getComboBoxWithPersonDTOData(List<PersonDTO> personDTOS) {
        ComboBox<PersonRecord> comboBox = new ComboBox<PersonRecord>();
        List<PersonRecord> sampleItems;
        sampleItems = personDTOS.stream().sorted((o1, o2) -> o1.getLastName().compareTo(o2.getFirstName())).map(o -> new PersonRecord(o, o.getState() + " " + o.getLastName())).collect(Collectors.toList());
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> item.label());
        comboBox.setLabel("Säufer");
        comboBox.setWidth("min-content");
        return comboBox;
    }

    public static String formatDateToDisplay(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String formatDoubleToEuro(double number) {
        return df.format(number) + " €";
    }

    public static Map<String, String> getMailTemplateVariables(PersonDTO personDTO, BillDTO billDTO) {

        Map<String, String> variables = new HashMap<>();
        variables.put("name", personDTO.getLastName());
        variables.put("user-balance", formatDoubleToEuro(personDTO.getBalance()));
        variables.put("red-count", billDTO.red() + " × " + formatDoubleToEuro(BillDTO.RED_VALUE));
        variables.put("blue-count", billDTO.blue() + " × " + formatDoubleToEuro(BillDTO.BLUE_VALUE));
        variables.put("white-count", billDTO.white() + " × " + formatDoubleToEuro(BillDTO.WHITE_VALUE));
        variables.put("green-count", billDTO.green() + " × " + formatDoubleToEuro(BillDTO.GREEN_VALUE));

        variables.put("red-subtotal", formatDoubleToEuro(billDTO.red() * BillDTO.RED_VALUE));
        variables.put("blue-subtotal", formatDoubleToEuro(billDTO.blue() * BillDTO.BLUE_VALUE));
        variables.put("white-subtotal", formatDoubleToEuro(billDTO.white() * BillDTO.WHITE_VALUE));
        variables.put("green-subtotal", formatDoubleToEuro(billDTO.green() * BillDTO.GREEN_VALUE));
        variables.put("sum", formatDoubleToEuro(Math.abs(billDTO.value())));
        return variables;
    }

    public static String replaceHtmlTemplateVariables(String someHtmlMessage, PersonDTO personDTO, BillDTO billDTO) {
        Map<String, String> variables = getMailTemplateVariables(personDTO, billDTO);

        for (String key: variables.keySet()) {
            String nextkey = "${" + key + "}";
            someHtmlMessage = someHtmlMessage.replace(nextkey, variables.get(key));
        }

        return someHtmlMessage;
    }
}

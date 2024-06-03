package com.sg.bierkasse.utils;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.vaadin.flow.component.combobox.ComboBox;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.GERMAN);
    public static ComboBox<PersonRecord> getComboBoxWithPersonDTOData(List<PersonDTO> personDTOS) {
        ComboBox<PersonRecord> comboBox = new ComboBox<>();
        List<PersonRecord> sampleItems;
        sampleItems = personDTOS.stream().sorted((o1, o2) -> o1.getLastName().compareTo(o2.getFirstName())).map(o -> new PersonRecord(o, o.getState() + " " + o.getLastName())).collect(Collectors.toList());
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(PersonRecord::label);
        comboBox.setLabel("Säufer");
        comboBox.setWidth("min-content");
        return comboBox;
    }

    public static ComboBox<UserState> getComboBoxWithStatusData() {
        ComboBox<UserState> comboBox = new ComboBox<>();
        List<UserState> sampleItems = List.of(UserState.values());

        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> item.fullName);
        return comboBox;
    }

    public static String formatDateToDisplay(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String formatDoubleToEuro(double number) {
        if (number == 0) {
            return "-";
        }
        return df.format(number) + " €";
    }

    public static Map<String, String> getMailTemplateVariables(PersonDTO personDTO, BillDTO billDTO) {
        Map<String, String> variables = new HashMap<>();
        variables.put("name", personDTO.getLastName());
        variables.put("current-date", formatDateToDisplay(new Date()));
        variables.put("user-balance", formatDoubleToEuro(personDTO.getBalance()));

        if (billDTO != null) {
            variables.put("red-count", String.valueOf(billDTO.red()));
            variables.put("blue-count", String.valueOf(billDTO.blue()));
            variables.put("white-count", String.valueOf(billDTO.white()));
            variables.put("green-count", String.valueOf(billDTO.green()));
        }

        variables.put("blue-price", formatDoubleToEuro(BillDTO.BLUE_VALUE));
        variables.put("red-price", formatDoubleToEuro(BillDTO.RED_VALUE));
        variables.put("white-price", formatDoubleToEuro(BillDTO.WHITE_VALUE));
        variables.put("green-price", formatDoubleToEuro(BillDTO.GREEN_VALUE));

        if (billDTO != null) {
            variables.put("blue-subtotal", formatDoubleToEuro(billDTO.blue() * BillDTO.BLUE_VALUE));
            variables.put("red-subtotal", formatDoubleToEuro(billDTO.red() * BillDTO.RED_VALUE));
            variables.put("white-subtotal", formatDoubleToEuro(billDTO.white() * BillDTO.WHITE_VALUE));
            variables.put("green-subtotal", formatDoubleToEuro(billDTO.green() * BillDTO.GREEN_VALUE));
            variables.put("sum", formatDoubleToEuro(Math.abs(billDTO.value())));
        }

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

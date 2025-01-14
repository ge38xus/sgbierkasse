package com.sg.bierkasse.utils;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.dtos.SpendeDTO;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.theme.lumo.LumoUtility;

import javax.swing.event.ChangeListener;
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
        if (date == null) {
            return " - ";
        }
        return simpleDateFormat.format(date);
    }

    public static String formatDoubleToEuro(double number) {
        if (number == 0) {
            return "-";
        }
        return df.format(number) + " €";
    }

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

    public static String replaceHtmlTemplateVariables(String someHtmlMessage, PersonDTO personDTO, SpendeDTO spendeDTO) {
        Map<String, String> variables = getMailTemplateVariables(personDTO, spendeDTO);

        for (String key : variables.keySet()) {
            String nextkey = "${" + key + "}";
            someHtmlMessage = someHtmlMessage.replace(nextkey, variables.get(key));
        }

        return someHtmlMessage;
    }

    public static HorizontalLayout createHorizontalRowLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.addClassName(LumoUtility.Gap.MEDIUM);
        layout.setWidth("100%");
        layout.getStyle().set("flex-grow", "1");
        return layout;
    }

    public static NumberField getEuroField(String label) {
        NumberField euroField = new NumberField();
        euroField.setLabel(label);
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        euroField.setSuffixComponent(euroSuffix);
        return euroField;
    }

    public static IntegerField getIntegerField(String label) {
        IntegerField integerField = new IntegerField();
        integerField.setStepButtonsVisible(true);
        integerField.setLabel(label);
        return integerField;
    }

    public static IntegerField getIntegerField(String label, HasValue.ValueChangeListener listener) {
        IntegerField ret = getIntegerField(label);
        ret.addValueChangeListener(listener);
        return ret;
    }
}

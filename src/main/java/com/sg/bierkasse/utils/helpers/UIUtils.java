package com.sg.bierkasse.utils.helpers;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.UserState;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.*;
import java.util.stream.Collectors;

public class UIUtils {
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

    public static Button getSaveButton(String label) {
        Button save = new Button();
        save.setText(label);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return save;
    }

    public static void showSuccessNotification() {
        Notification notification = Notification.show("Submitted!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}

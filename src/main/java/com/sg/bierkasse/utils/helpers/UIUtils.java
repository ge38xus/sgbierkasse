package com.sg.bierkasse.utils.helpers;

import com.sg.bierkasse.utils.UserState;
import com.vaadin.flow.component.HasValue;
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

public class UIUtils {
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
        layout.getStyle().set("flex-wrap", "wrap");
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

    public static NumberField getEuroField(String label, HasValue.ValueChangeListener listener) {
        NumberField euroField = getEuroField(label);
        euroField.addValueChangeListener(listener);
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

    public static void showSuccessNotification(String message) {
        Notification notification = Notification.show(message);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}

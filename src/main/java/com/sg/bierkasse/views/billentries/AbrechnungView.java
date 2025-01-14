package com.sg.bierkasse.views.billentries;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.utils.EmailTemplates;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.Utils;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Date;

import static com.sg.bierkasse.dtos.BillDTO.*;
import static com.sg.bierkasse.utils.Utils.createHorizontalRowLayout;

@PageTitle("Abrechnung")
@Route(value = "/", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class AbrechnungView extends Composite<VerticalLayout> {
    private final IntegerField blue;
    private final IntegerField red;
    private final IntegerField white;
    private final IntegerField green;
    private final NumberField greenValue;

    public AbrechnungView(PersonServiceImpl personService) {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        ComboBox<PersonRecord> comboBox = Utils.getComboBoxWithPersonDTOData(personService.findAll());

        HorizontalLayout layoutRowBottles = createHorizontalRowLayout();
        HorizontalLayout layoutRowWine = createHorizontalRowLayout();
        HorizontalLayout layoutRowSum = createHorizontalRowLayout();
        HorizontalLayout layoutRowControls = createHorizontalRowLayout();

        H4 value = new H4();
        value.setText("Summe: 0€");
        HasValue.ValueChangeListener valueChangeListener = valueChangeEvent -> {
            value.setText("Summe: " + calculateValue() + "€");
        };

        blue = Utils.getIntegerField("Blaue Ringe", valueChangeListener);
        red = Utils.getIntegerField("Rote Ringe", valueChangeListener);
        white = Utils.getIntegerField("Weiße Ringe", valueChangeListener);
        green = Utils.getIntegerField("Grüne Ringe", valueChangeListener);
        greenValue = Utils.getEuroField("Grün Wert");
        greenValue.setValue(GREEN_VALUE_DEFAULT);

        TextField textField = new TextField();
        textField.setLabel("Beschreibung Grün");
        textField.setValue(GREEN_VALUE_DEFAULT_TEXT);

        Button save = new Button();
        save.addClickListener(o -> {
            int redCnt = red.getValue() != null ? red.getValue().intValue() : 0;
            int blueCnt = blue.getValue() != null ? blue.getValue().intValue() : 0;
            int whiteCnt = white.getValue() != null ? white.getValue().intValue() : 0;
            int greenCnt = green.getValue() != null ? green.getValue().intValue() : 0;
            double greenV = greenValue.getValue();

            BillDTO billDTO = new BillDTO(redCnt, blueCnt, whiteCnt, greenCnt, greenV, textField.getValue(), -calculateValue(), new Date());
            PersonDTO personToChange = comboBox.getValue().value();
            personService.pushBill(personToChange, billDTO, EmailTemplates.DRINKS_OVERVIEW);
            comboBox.setValue(comboBox.getEmptyValue());
            blue.setValue(0);
            red.setValue(0);
            white.setValue(0);
            green.setValue(0);
            Notification notification = Notification
                    .show("Submitted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Konsum aufschreiben");
        h3.setWidth("100%");
        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn3.setFlexGrow(1.0, layoutRowBottles);

        save.setText("Save");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layoutRowBottles.add(blue);
        layoutRowBottles.add(red);
        layoutRowBottles.add(white);
        layoutRowWine.add(green);
        layoutRowWine.add(greenValue);
        layoutRowWine.add(textField);
        layoutRowSum.add(value);
        layoutRowControls.add(save);

        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(comboBox);
        layoutColumn3.add(layoutRowBottles);
        layoutColumn3.add(layoutRowWine);
        layoutColumn3.add(layoutRowSum);
        layoutColumn3.add(layoutRowControls);


    }

    public double calculateValue() {
        int redCnt = red.getValue() != null ? red.getValue().intValue() : 0;
        int blueCnt = blue.getValue() != null ? blue.getValue().intValue() : 0;
        int whiteCnt = white.getValue() != null ? white.getValue().intValue() : 0;
        int greenCnt = green.getValue() != null ? green.getValue().intValue() : 0;
        double greenV = greenValue.getValue();

        return redCnt * RED_VALUE +
                blueCnt * BLUE_VALUE +
                whiteCnt * WHITE_VALUE +
                greenCnt * greenV;
    }
}

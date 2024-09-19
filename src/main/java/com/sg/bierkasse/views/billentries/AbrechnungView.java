package com.sg.bierkasse.views.billentries;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.utils.EmailTemplates;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.Utils;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.util.Date;

import static com.sg.bierkasse.dtos.BillDTO.*;

@PageTitle("Abrechnung")
@Route(value = "/", layout = MainLayout.class)
@Uses(Icon.class)
public class AbrechnungView extends Composite<VerticalLayout> {

    private PersonServiceImpl personService;

    public AbrechnungView(PersonServiceImpl personService) {
        this.personService = personService;
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        ComboBox comboBox = Utils.getComboBoxWithPersonDTOData(personService.findAll());

        HorizontalLayout layoutRow = new HorizontalLayout();
        NumberField blue = new NumberField();
        NumberField red = new NumberField();
        NumberField white = new NumberField();
        NumberField green = new NumberField();
        NumberField greenValue = new NumberField();
        greenValue.setValue(GREEN_VALUE_DEFAULT);
        greenValue.setLabel("Grün Wert");
        TextField textField = new TextField();
        textField.setLabel("Beschreibung Grün");
        textField.setValue(GREEN_VALUE_DEFAULT_TEXT);
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button save = new Button();
        save.addClickListener(o -> {
            int redCnt = red.getValue() != null ? red.getValue().intValue() : 0;
            int blueCnt = blue.getValue() != null ? blue.getValue().intValue() : 0;
            int whiteCnt = white.getValue() != null ? white.getValue().intValue() : 0;
            int greenCnt = green.getValue() != null ? green.getValue().intValue() : 0;
            double greenV = greenValue.getValue().doubleValue();

            double value = redCnt * RED_VALUE +
                    blueCnt * BLUE_VALUE +
                    whiteCnt * WHITE_VALUE +
                    greenCnt * greenV;
            BillDTO billDTO = new BillDTO(redCnt, blueCnt, whiteCnt, greenCnt, greenV, textField.getValue(), -value, new Date());
            PersonDTO personToChange = ((PersonRecord)comboBox.getValue()).value();
            personService.pushBill(personToChange, billDTO, EmailTemplates.DRINKS_OVERVIEW);
            comboBox.setValue(comboBox.getEmptyValue());
            blue.setValue(0.0);
            red.setValue(0.0);
            white.setValue(0.0);
            green.setValue(0.0);
            Notification notification = Notification
                    .show("Submitted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        Button buttonSecondary = new Button();
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
        layoutRow.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        blue.setLabel("Blau");
        blue.setWidth("min-content");
        red.setLabel("Rot");
        red.setWidth("min-content");
        white.setLabel("Weiß");
        white.setWidth("min-content");
        green.setLabel("Grün");
        green.setWidth("min-content");
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        save.setText("Save");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(comboBox);
        layoutColumn3.add(layoutRow);
        layoutRow.add(blue);
        layoutRow.add(red);
        layoutRow.add(white);
        layoutRow.add(green);
        layoutRow.add(greenValue);
        layoutRow.add(textField);
        layoutColumn2.add(layoutRow2);
        layoutRow2.add(save);
        layoutRow2.add(buttonSecondary);
    }
}

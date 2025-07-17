package com.sg.bierkasse.views.billentries;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.EmailTemplates;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.utils.helpers.UIUtils;
import com.sg.bierkasse.views.components.BenachrichtigungCheckbox;
import com.sg.bierkasse.views.components.MyIntegerField;
import com.sg.bierkasse.views.components.UserComboBox;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;

import static com.sg.bierkasse.dtos.BillDTO.*;
import static com.sg.bierkasse.utils.helpers.UIUtils.createHorizontalRowLayout;

@PageTitle("Abrechnung")
@Route(value = "/count", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class AbrechnungView extends Composite<VerticalLayout> {

    private final UserComboBox userComboBox;
    private final BenachrichtigungCheckbox benachrichtigungCheckbox;
    private final MyIntegerField blue;
    private final MyIntegerField red;
    private final MyIntegerField white;
    private final MyIntegerField green;
    private final NumberField greenValue;
    private final TextField textField;
    private final TextField description;
    private final String DESCRIPTION_VALUE_DEFAULT_TEXT = "Hacken Abrechnung";

    private final PersonService personService;

    public AbrechnungView(PersonService personService) {
        this.personService = personService;

        userComboBox = new UserComboBox(personService);
        benachrichtigungCheckbox = new BenachrichtigungCheckbox(userComboBox);

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        VerticalLayout layoutColumn3 = new VerticalLayout();

        HorizontalLayout layoutRowBottles = createHorizontalRowLayout();
        HorizontalLayout layoutRowWine = createHorizontalRowLayout();
        HorizontalLayout layoutRowDesc = createHorizontalRowLayout();
        HorizontalLayout layoutRowSum = createHorizontalRowLayout();
        HorizontalLayout layoutRowControls = createHorizontalRowLayout();

        H4 value = new H4();
        value.setText("Summe: 0€");
        HasValue.ValueChangeListener valueChangeListener = valueChangeEvent -> value.setText("Summe: " + calculateValue() + "€");

        blue = new MyIntegerField("Blaue Ringe", valueChangeListener);
        red = new MyIntegerField("Rote Ringe", valueChangeListener);
        white = new MyIntegerField("Weiße Ringe", valueChangeListener);
        green = new MyIntegerField("Grüne Ringe", valueChangeListener);
        greenValue = UIUtils.getEuroField("Grün Wert", valueChangeListener);
        greenValue.setValue(GREEN_VALUE_DEFAULT);

        textField = new TextField();
        textField.setLabel("Beschreibung Grün");
        textField.setValue(GREEN_VALUE_DEFAULT_TEXT);

        description = new TextField();
        description.setLabel("Beschreibung");
        description.setValue(DESCRIPTION_VALUE_DEFAULT_TEXT);

        Button save = new Button();
        save.addClickListener(o -> saveAndResetForm());

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
        layoutRowDesc.add(description);
        layoutRowSum.add(value);
        layoutRowControls.add(save);
        layoutRowControls.add(benachrichtigungCheckbox);

        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(userComboBox);
        layoutColumn3.add(layoutRowBottles);
        layoutColumn3.add(layoutRowWine);
        layoutColumn3.add(layoutRowDesc);
        layoutColumn3.add(layoutRowSum);
        layoutColumn3.add(layoutRowControls);


    }

    private void saveAndResetForm() {
        try {
            BillDTO billDTO = createBillDTO();
            personService.pushBill(userComboBox.getSelected(), billDTO, EmailTemplates.DRINKS_OVERVIEW, benachrichtigungCheckbox.getValue());

            userComboBox.reset();
            blue.setValue(0);
            red.setValue(0);
            white.setValue(0);
            green.setValue(0);
            description.setValue(DESCRIPTION_VALUE_DEFAULT_TEXT);

            Notification notification = Notification.show("Abrechnung gespeichert!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (MessagingException | IOException e) {
            Notification notification = Notification.show("Fehler: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    public double calculateValue() {
        return red.getIntValue() * RED_VALUE +
                blue.getIntValue() * BLUE_VALUE +
                white.getIntValue() * WHITE_VALUE +
                green.getIntValue() * greenValue.getValue();
    }

    public BillDTO createBillDTO() {
        return new BillDTO(red.getIntValue(), blue.getIntValue(), white.getIntValue(), green.getIntValue(), greenValue.getValue(), textField.getValue(), -calculateValue(), new Date(), description.getValue());
    }
}

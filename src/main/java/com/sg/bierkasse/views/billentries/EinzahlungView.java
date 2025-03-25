package com.sg.bierkasse.views.billentries;


import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.EmailTemplates;
import com.sg.bierkasse.utils.helpers.UIUtils;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.views.components.BenachrichtigungCheckbox;
import com.sg.bierkasse.views.components.BillOverviewComponent;
import com.sg.bierkasse.views.components.UserComboBox;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;

@PageTitle("Ein-/Auszahlung")
@Route(value = "book-money", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class EinzahlungView extends Composite<VerticalLayout> {

    private final UserComboBox userComboBox;
    private final BenachrichtigungCheckbox benachrichtigungCheckbox;
    private final NumberField price;
    private final PersonService personService;

    private final BillOverviewComponent grid;

    public EinzahlungView(PersonService personService) {
        this.personService = personService;
        this.userComboBox = new UserComboBox(personService);
        this.grid = new BillOverviewComponent(userComboBox);
        this.benachrichtigungCheckbox = new BenachrichtigungCheckbox(userComboBox);
        this.price = UIUtils.getEuroField("Ein-/Ausgezahlt");
        this.price.setHelperText("Für Auszahlungen / Abbuchungen schreibe den Betrag mit '-'");

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();

        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Geld (Ab)buchung");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonPrimary.addClickListener(o -> saveAndResetForm());

        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(userComboBox);
        formLayout2Col.add(price);
        formLayout2Col.add(benachrichtigungCheckbox);
        layoutColumn2.add(layoutRow);
        layoutColumn2.add(grid);
        layoutRow.add(buttonPrimary);
    }

    private void saveAndResetForm() {
        try {
            BillDTO billDTO = new BillDTO(0, 0, 0, 0,0, "", price.getValue(), new Date());

            if (price.getValue() > 0) {
                personService.pushBill(userComboBox.getSelected(), billDTO, EmailTemplates.BOOK_IN_MONEY, benachrichtigungCheckbox.getValue());
                UIUtils.showSuccessNotification(price.getValue() + " zu Konto von " + userComboBox.getSelected().getLastName() + " gebucht." );
            } else {
                personService.pushBill(userComboBox.getSelected(), billDTO, EmailTemplates.BOOK_OUT_MONEY, benachrichtigungCheckbox.getValue());
                UIUtils.showSuccessNotification(price.getValue() + " von Konto von " + userComboBox.getSelected().getLastName() + " abgebucht.");
            }

            price.setValue(0.0);
            userComboBox.reset();
            grid.reset();
        } catch (MessagingException | IOException e) {
            Notification notification = Notification.show("Fehler: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}

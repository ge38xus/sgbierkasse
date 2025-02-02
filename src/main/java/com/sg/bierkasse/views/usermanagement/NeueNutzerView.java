package com.sg.bierkasse.views.usermanagement;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.UserState;
import com.sg.bierkasse.utils.helpers.UIUtils;
import com.sg.bierkasse.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;

@PageTitle("Neue Nutzer")
@Route(value = "person-form", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class NeueNutzerView extends Composite<VerticalLayout> {

    public NeueNutzerView(PersonService personService) {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField firstName = new TextField();
        TextField lastName = new TextField();
        EmailField emailField = new EmailField();
        ComboBox<UserState> state = UIUtils.getComboBoxWithStatusData();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Checkbox checkbox = new Checkbox();

        buttonPrimary.addClickListener(o -> {
            PersonDTO personDTO = new PersonDTO(null,
                    firstName.getValue(),
                    lastName.getValue(),
                    emailField.getValue(),
                    state.getValue().name,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    checkbox.getValue(),
                    false
            );
            personService.save(personDTO);
            firstName.setValue("");
            lastName.setValue("");
            emailField.setValue("");
            state.setValue(state.getEmptyValue());
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
        h3.setText("Personal Information");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        firstName.setLabel("First Name");
        lastName.setLabel("Last Name");
        emailField.setLabel("Email");
        state.setLabel("Status");
        state.setWidth("min-content");
        checkbox.setLabel("Soll in Excel Export auftauchen?");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(firstName);
        formLayout2Col.add(lastName);
        formLayout2Col.add(emailField);
        formLayout2Col.add(state);
        formLayout2Col.add(checkbox);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
}

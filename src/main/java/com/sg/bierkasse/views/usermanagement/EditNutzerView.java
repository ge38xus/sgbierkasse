package com.sg.bierkasse.views.usermanagement;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.UserState;
import com.sg.bierkasse.utils.Utils;
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

@PageTitle("Neue Nutzer")
@Route(value = "edit-person-form", layout = MainLayout.class)
@Uses(Icon.class)
public class EditNutzerView extends Composite<VerticalLayout> {

    private PersonDTO personDTO;
    public EditNutzerView(PersonServiceImpl personService) {

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField firstName = new TextField();
        TextField lastName = new TextField();
        EmailField emailField = new EmailField();
        ComboBox<UserState> state = Utils.getComboBoxWithStatusData();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Checkbox excelRelevant = new Checkbox();
        Checkbox berichtReceiver = new Checkbox();
        state.addValueChangeListener(o -> {
            if (state.getValue().equals(UserState.CB) ||
                    state.getValue().equals(UserState.F) ||
                    state.getValue().equals(UserState.iaCB)) {
                berichtReceiver.setValue(true);
                berichtReceiver.setEnabled(false);
            } else {
                berichtReceiver.setEnabled(true);
                berichtReceiver.setValue(false);
            }
        });

        ComboBox<PersonRecord> comboBox = Utils.getComboBoxWithPersonDTOData(personService.findAll());
        comboBox.addValueChangeListener(o -> {
            if (!comboBox.isEmpty()) {
                personDTO = comboBox.getValue().value();
                firstName.setValue(personDTO.getFirstName());
                lastName.setValue(personDTO.getLastName());
                emailField.setValue(personDTO.getEmail());
                state.setValue(UserState.valueOf(personDTO.getState()));
                excelRelevant.setValue(personDTO.isExcelRelevant());
                berichtReceiver.setValue(personDTO.isBerichtReceiver());
            }
        });

        buttonPrimary.addClickListener(o -> {
            personDTO = new PersonDTO(personDTO.getId(),
                    firstName.getValue(),
                    lastName.getValue(),
                    emailField.getValue(),
                    state.getValue().name,
                    personDTO.getBills(),
                    personDTO.getInvoices(),
                    personDTO.getSpenden(),
                    excelRelevant.getValue(),
                    berichtReceiver.getValue()
            );
            personService.update(personDTO);
            Notification notification = Notification.show("Submitted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

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
        excelRelevant.setLabel("Soll in Excel Export auftauchen?");
        berichtReceiver.setLabel("Soll Bericht verschickt bekommen?");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(comboBox);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(firstName);
        formLayout2Col.add(lastName);
        formLayout2Col.add(emailField);
        formLayout2Col.add(state);
        formLayout2Col.add(excelRelevant);
        formLayout2Col.add(berichtReceiver);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
    }
}

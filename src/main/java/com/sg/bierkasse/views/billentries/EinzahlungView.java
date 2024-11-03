package com.sg.bierkasse.views.billentries;


import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.utils.EmailTemplates;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.Utils;
import com.sg.bierkasse.views.components.BillOverviewComponent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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

import java.util.Date;

@PageTitle("Einzahlung")
@Route(value = "book-money", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class EinzahlungView extends Composite<VerticalLayout> {

    public EinzahlungView(PersonServiceImpl personService) {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        Grid<BillDTO> grid = new BillOverviewComponent();
        ComboBox<PersonRecord> comboBox = Utils.getComboBoxWithPersonDTOData(personService.findAll());
        comboBox.addValueChangeListener(o -> {
            if (!comboBox.isEmpty()) {
                PersonDTO personToChange = comboBox.getValue().value();
                grid.setItems(personToChange.getBills().stream().sorted().toList());
            }
        });
        NumberField price = new NumberField();
        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Benachrichtigen");
        checkbox.setValue(true);
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
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

        price.setLabel("Eingezahlt");
        price.setWidth("min-content");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonPrimary.addClickListener(o -> {
            double value = price.getValue();
            BillDTO billDTO = new BillDTO(0, 0, 0, 0,0, "", value, new Date());
            PersonDTO personToChange = comboBox.getValue().value();
            if (value > 0) {
                personService.pushBill(personToChange, billDTO, EmailTemplates.BOOK_IN_MONEY, checkbox.getValue());
            } else if (value < 0) {
                personService.pushBill(personToChange, billDTO, EmailTemplates.BOOK_OUT_MONEY, checkbox.getValue());
            } else {
                Notification notification = Notification
                        .show("Failed!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            price.setValue(0.0);
            comboBox.setValue(comboBox.getEmptyValue());
            Notification notification = Notification
                    .show("Submitted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(comboBox);
        formLayout2Col.add(price);
        formLayout2Col.add(checkbox);
        layoutColumn2.add(layoutRow);
        layoutColumn2.add(grid);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
}

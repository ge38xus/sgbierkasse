package com.sg.bierkasse.views.overviews;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.dtos.RechnungDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.Utils;
import com.sg.bierkasse.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@PageTitle("Rechnungen")
@Route(value = "invoices", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class RechnungView extends Composite<VerticalLayout> {

    PersonService personService;

    public RechnungView(PersonService personService) {
        VerticalLayout layoutColumn = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        this.personService = personService;

        Grid<RechnungDTO> grid = new Grid<>(RechnungDTO.class, false);
        grid.addColumn(RechnungDTO::formattedDate).setHeader("Datum");
        grid.addColumn(RechnungDTO::formattedValue).setHeader("Summe");
        grid.addColumn(RechnungDTO::description).setHeader("Beschreibung");
        grid.addColumn(RechnungDTO::privateMoneyUsed).setHeader("Selbst Bezahlt?");

        grid.setItems(
                personService.getAllRechnungDTOs()
        );

        ComboBox<PersonRecord> comboBox = Utils.getComboBoxWithPersonDTOData(personService.findAll());

        DatePicker datePicker = new DatePicker("Rechnungsdatum");
        datePicker.setLocale(new Locale("de", "DE"));
        datePicker.setVisible(false);
        NumberField numberField = new NumberField("Summe");
        numberField.setVisible(false);
        TextField descriptionField = new TextField("Beschreibung");
        descriptionField.setWidth("200%");
        descriptionField.setVisible(false);
        Checkbox checkbox = new Checkbox("Selbst bezahlt?");
        checkbox.setVisible(false);
        Button addRechnung = new Button("Save");
        addRechnung.setVisible(false);

        comboBox.addValueChangeListener(o -> {
            String chosenPersonId = (o.getValue()).value().getId();
            PersonDTO chosenPerson = personService.findOne(chosenPersonId);
            grid.setItems(chosenPerson.getInvoices());
            datePicker.setVisible(true);
            numberField.setVisible(true);
            descriptionField.setVisible(true);
            checkbox.setVisible(true);
            addRechnung.setVisible(true);
        });

        addRechnung.addClickListener(o -> {
            Date date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            RechnungDTO rechnungDTO = new RechnungDTO(numberField.getValue(), checkbox.getValue(), date, descriptionField.getValue());
            PersonDTO personToChange = comboBox.getValue().value();

            personService.pushRechnung(personToChange, rechnungDTO);
            datePicker.setValue(null);
            descriptionField.setValue("");
            numberField.setValue(0.0);

            grid.setItems(personService.findOne(personToChange.getId()).getInvoices());

            Notification notification = Notification
                    .show("Submitted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        horizontalLayout.add(comboBox);
        horizontalLayout.add(datePicker);
        horizontalLayout.add(numberField);
        horizontalLayout.add(descriptionField);
        horizontalLayout.add(checkbox);
        horizontalLayout.add(addRechnung);

        layoutColumn.add(horizontalLayout);
        layoutColumn.add(grid);
        layoutColumn.setHeight("100%");
        getContent().add(layoutColumn);
    }
}

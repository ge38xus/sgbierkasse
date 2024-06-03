package com.sg.bierkasse.views.overviews;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.dtos.SpendeDTO;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.Utils;
import com.sg.bierkasse.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@PageTitle("Spenden")
@Route(value = "spenden", layout = MainLayout.class)
@Uses(Icon.class)
public class SpendeView extends Composite<VerticalLayout> {

    PersonServiceImpl personService;

    public SpendeView(PersonServiceImpl personService) {
        VerticalLayout layoutColumn = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        this.personService = personService;

        Grid<SpendeDTO> grid = new Grid<>(SpendeDTO.class, false);
        grid.addColumn(SpendeDTO::formattedDate).setHeader("Datum");
        grid.addColumn(SpendeDTO::formattedValue).setHeader("Summe");
        grid.addColumn(SpendeDTO::formattedDatePayedOn).setHeader("Eingezahlt am");
        grid.addColumn(SpendeDTO::occasion).setHeader("Beschreibung");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, spende) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                            ButtonVariant.LUMO_SUCCESS,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> personService.paySpende(spende));
                })).setHeader("Payed");

        grid.setItems(
                personService.getAllSpenden()
        );

        ComboBox<PersonRecord> comboBox = Utils.getComboBoxWithPersonDTOData(personService.findAll());

        DatePicker datePicker = new DatePicker("SpendeDatum");
        datePicker.setLocale(new Locale("de", "DE"));
        datePicker.setVisible(false);
        NumberField numberField = new NumberField("Summe");
        numberField.setVisible(false);
        TextField descriptionField = new TextField("Beschreibung");
        descriptionField.setWidth("200%");
        descriptionField.setVisible(false);
        Button addRechnung = new Button("Save");
        addRechnung.setVisible(false);

        comboBox.addValueChangeListener(o -> {
            String chosenPersonId = (o.getValue()).value().getId();
            PersonDTO chosenPerson = personService.findOne(chosenPersonId);
            grid.setItems(chosenPerson.getSpenden());
            datePicker.setVisible(true);
            numberField.setVisible(true);
            descriptionField.setVisible(true);
            addRechnung.setVisible(true);
        });

        addRechnung.addClickListener(o -> {
            Date date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            SpendeDTO spendeDTO = new SpendeDTO(numberField.getValue(), date, null ,descriptionField.getValue());
            PersonDTO personToChange = comboBox.getValue().value();

//            personService.pushSpende(personToChange, spendeDTO);
            datePicker.setValue(null);
            descriptionField.setValue("");
            numberField.setValue(0.0);

            grid.setItems(personService.findOne(personToChange.getId()).getSpenden());

            Notification notification = Notification
                    .show("Submitted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        horizontalLayout.add(comboBox);
        horizontalLayout.add(datePicker);
        horizontalLayout.add(numberField);
        horizontalLayout.add(descriptionField);
        horizontalLayout.add(addRechnung);

        layoutColumn.add(horizontalLayout);
        layoutColumn.add(grid);
        layoutColumn.setHeight("100%");
        getContent().add(layoutColumn);
    }
}

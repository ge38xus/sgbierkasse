package com.sg.bierkasse.views.overviews;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.dtos.RechnungDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.helpers.UIUtils;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.views.components.MyDatePicker;
import com.sg.bierkasse.views.components.UserComboBox;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Rechnungen")
@Route(value = "invoices", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class RechnungView extends Composite<VerticalLayout> {

    private final PersonService personService;
    private final MyDatePicker datePicker = new MyDatePicker("Rechnungs Datum");
    private final NumberField price = UIUtils.getEuroField("Eingezahlt");
    private final TextField descriptionField = new TextField("Beschreibung");
    private final Checkbox checkbox = new Checkbox("Selbst bezahlt?");
    private final Button saveButton = new Button("Save");
    private final Grid<RechnungDTO> grid = new Grid<>(RechnungDTO.class, false);
    private final UserComboBox userComboBox;

    public RechnungView(PersonService personService) {
        this.personService = personService;
        this.userComboBox = new UserComboBox(personService);

        this.initPage();

        userComboBox.addValueChangeListener(o -> {
            PersonDTO chosenPerson = userComboBox.getSelected();
            grid.setItems(chosenPerson.getInvoices());
            setPanelVisibility(true);
        });

        saveButton.addClickListener(o -> {
            RechnungDTO rechnungDTO = new RechnungDTO(price.getValue(), checkbox.getValue(), datePicker.getPickedDate(), descriptionField.getValue());
            PersonDTO personToChange = userComboBox.getSelected();

            personService.pushRechnung(personToChange, rechnungDTO);
            grid.setItems(personService.findOne(personToChange.getId()).getInvoices());

            this.resetForm();
            UIUtils.showSuccessNotification();
        });
    }

    private void initPage() {
        VerticalLayout layoutColumn = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        grid.addColumn(RechnungDTO::formattedDate).setHeader("Datum");
        grid.addColumn(RechnungDTO::formattedValue).setHeader("Summe");
        grid.addColumn(RechnungDTO::description).setHeader("Beschreibung");
        grid.addColumn(RechnungDTO::privateMoneyUsed).setHeader("Selbst Bezahlt?");
        grid.setItems(personService.getAllRechnungDTOs());

        descriptionField.setWidth("200%");
        setPanelVisibility(false);

        horizontalLayout.add(userComboBox);
        horizontalLayout.add(datePicker);
        horizontalLayout.add(price);
        horizontalLayout.add(descriptionField);
        horizontalLayout.add(checkbox);
        horizontalLayout.add(saveButton);

        layoutColumn.add(horizontalLayout);
        layoutColumn.add(grid);
        layoutColumn.setHeight("100%");
        getContent().add(layoutColumn);
    }

    private void resetForm(){
        datePicker.setValue(null);
        descriptionField.setValue("");
        price.setValue(0.0);
        userComboBox.reset();

        setPanelVisibility(false);
    }

    private void setPanelVisibility(boolean visibility) {
        datePicker.setVisible(visibility);
        price.setVisible(visibility);
        descriptionField.setVisible(visibility);
        checkbox.setVisible(visibility);
        saveButton.setVisible(visibility);
    }
}

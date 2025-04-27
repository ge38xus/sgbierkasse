package com.sg.bierkasse.views.components;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.helpers.FormatUtils;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.List;
import java.util.stream.Collectors;

public class UserComboBox extends ComboBox<PersonRecord> {

    private final boolean displayBalance;

    public UserComboBox(PersonService personService) {
        this(personService, false);
    }

    public UserComboBox(PersonService personService, boolean displayBalance) {
        super();
        this.displayBalance = displayBalance;
        List<PersonRecord> sampleItems = personService.findAll().stream()
                .sorted((o1, o2) -> o1.getLastName().compareTo(o2.getFirstName()))
                .map(this::mapToRecord)
                .collect(Collectors.toList());
        setItems(sampleItems);
        setItemLabelGenerator(PersonRecord::label);
        setLabel("Säufer");
        if (displayBalance) {
            getStyle().set("--vaadin-combo-box-overlay-width", "250px");
            setWidth("250px");
        }
    }

    public void reset() {
        setValue(getEmptyValue());
    }

    public PersonDTO getSelected() {
        return getValue().value();
    }

    private PersonRecord mapToRecord(PersonDTO o) {
        if (displayBalance) {
            return new PersonRecord(o, o.getState() + " " + o.getLastName() + " (" + FormatUtils.formatDoubleToEuro(o.getBalance()) + ")");
        } else {
            return new PersonRecord(o, o.getState() + " " + o.getLastName());
        }
    }
}

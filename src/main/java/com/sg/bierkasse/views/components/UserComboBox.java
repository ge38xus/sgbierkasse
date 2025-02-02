package com.sg.bierkasse.views.components;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.PersonRecord;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.List;
import java.util.stream.Collectors;

public class UserComboBox extends ComboBox<PersonRecord> {

    public UserComboBox(PersonService personService) {
        super();
        List<PersonRecord> sampleItems = personService.findAll().stream().sorted((o1, o2) -> o1.getLastName().compareTo(o2.getFirstName())).map(o -> new PersonRecord(o, o.getState() + " " + o.getLastName())).collect(Collectors.toList());
        setItems(sampleItems);
        setItemLabelGenerator(PersonRecord::label);
        setLabel("Säufer");
        setWidth("min-content");
    }

    public void reset() {
        setValue(getEmptyValue());
    }

    public PersonDTO getSelected() {
        return getValue().value();
    }
}

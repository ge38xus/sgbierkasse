package com.sg.bierkasse.views.components;

import com.sg.bierkasse.dtos.PersonDTO;
import com.vaadin.flow.component.checkbox.Checkbox;

public class BenachrichtigungCheckbox extends Checkbox {

    private static final String DEFAULT_TEXT = "Benachrichtigen";
    private static final String DEFAULT_NO_EMAIL_TEXT = "(email nicht hintergelegt)";

    public BenachrichtigungCheckbox(UserComboBox userComboBox) {
        super();
        setLabel(DEFAULT_TEXT);
        setValue(true);

        userComboBox.addValueChangeListener(o -> {
            if (!userComboBox.isEmpty()) {
                PersonDTO personToChange = userComboBox.getValue().value();
                checkBenachrichtigungPossibilityForPerson(personToChange);
            }
        });
    }

    private void checkBenachrichtigungPossibilityForPerson(PersonDTO personDTO) {
        if (getValue() && personDTO.getEmail().isEmpty()) {
            setValue(false);
            setEnabled(false);
            setLabel(DEFAULT_TEXT + " " + DEFAULT_NO_EMAIL_TEXT);
        } else {
            setValue(true);
            setEnabled(true);
            setLabel(DEFAULT_TEXT);
        }
    }
}

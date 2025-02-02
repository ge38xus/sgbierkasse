package com.sg.bierkasse.views.components;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.IntegerField;

public class MyIntegerField extends IntegerField {

    public MyIntegerField(String label, HasValue.ValueChangeListener listener) {
        this(label);
        addValueChangeListener(listener);
    }

    public MyIntegerField(String label) {
        super();
        this.setStepButtonsVisible(true);
        this.setLabel(label);
    }

    public Integer getIntValue() {
        return getValue() != null ? getValue() : 0;
    }
}

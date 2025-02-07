package com.sg.bierkasse.views.components;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class MyDatePicker extends DatePicker {

    public MyDatePicker(String label) {
        super(label);
        setLocale(new Locale("de", "DE"));
    }

    public Date getPickedDate() {
        return Date.from(getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

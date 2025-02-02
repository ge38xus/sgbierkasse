package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.SpendeEntity;
import com.sg.bierkasse.utils.helpers.FormatUtils;

import java.util.Date;

public record SpendeDTO(
        double value,
        Date date,
        Date payedOn,
        String occasion,
        boolean used,
        String usedFor
) {

    public SpendeDTO(SpendeEntity b) {
        this(b.getValue(), b.getDate(), b.getPayedOn(), b.getOccasion(), b.isUsed(), b.getUsedFor());
    }

    public SpendeEntity toSpendeEntity() {
        return new SpendeEntity(value, date, payedOn, occasion, used, usedFor);
    }

    public String formattedDate() {
        return FormatUtils.formatDateToDisplay(date);
    }

    public String formattedDatePayedOn() {
        return FormatUtils.formatDateToDisplay(payedOn);
    }

    public String formattedValue() {
        return FormatUtils.formatDoubleToEuro(value);
    }

    public int compareTo(Date other){
        return this.date.compareTo(other);
    }
}

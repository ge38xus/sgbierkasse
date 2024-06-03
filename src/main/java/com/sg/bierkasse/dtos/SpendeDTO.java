package com.sg.bierkasse.dtos;

import com.sg.bierkasse.utils.Utils;

import java.util.Date;

public record SpendeDTO(
        double value,
        Date date,
        Date payedOn,
        String occasion
) {

//    public SpendeDTO(SpendeEntity b) {
//        this(b.getValue(), b.getDate(), b.getPayedOn(), b.getOccasion());
//    }
//
//    public SpendeEntity toSpendeEntity() {
//        return new SpendeEntity(value, date, payedOn, occasion);
//    }

    public String formattedDate() {
        return Utils.formatDateToDisplay(date);
    }

    public String formattedDatePayedOn() {
        return Utils.formatDateToDisplay(payedOn);
    }

    public String formattedValue() {
        return Utils.formatDoubleToEuro(value);
    }

    public int compareTo(Date other){
        return this.date.compareTo(other);
    }
}

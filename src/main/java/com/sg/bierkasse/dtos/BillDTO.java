package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.BillEntity;
import com.sg.bierkasse.utils.Utils;

import java.util.Date;

public record BillDTO(
        int red,
        int blue,
        int white,
        int green,
        double value,
        Date date) {

    public static final double BLUE_VALUE = 0.5;
    public static final double RED_VALUE = 1;
    public static final double WHITE_VALUE = 1.1;
    public static final double GREEN_VALUE = 5;

    public BillDTO(BillEntity b) {
        this(b.getRed(), b.getBlue(), b.getWhite(), b.getGreen(), b.getValue(), b.getDate());
    }

    public BillEntity toBillEntity() {
        return new BillEntity(red, blue, white, green, value, date);
    }

    public String formattedDate() {
        return Utils.formatDateToDisplay(date);
    }

    public String formattedValue() {
        return Utils.formatDoubleToEuro(value);
    }

    public boolean isConsumptionBill(){
        return red > 0 || blue > 0 || white > 0 || green > 0;
    }

    public int compareTo(Date other){
        return this.date.compareTo(other);
    }
}

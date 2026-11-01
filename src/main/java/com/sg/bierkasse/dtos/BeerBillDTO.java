package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.bills.BeerBillEntity;
import com.sg.bierkasse.utils.helpers.FormatUtils;

import java.util.Date;

public record BeerBillDTO(
        int red,
        int blue,
        int white,
        int green,
        double greenValue,
        String greenText,
        double value,
        Date date,
        String descr) implements Comparable<BeerBillDTO> {

    public static final double BLUE_VALUE = 0.6;
    public static final double RED_VALUE = 1.15;
    public static final double WHITE_VALUE = 1;
    public static final double GREEN_VALUE_DEFAULT = 5;
    public static final String GREEN_VALUE_DEFAULT_TEXT = "Ring Grün";

    public BeerBillDTO(BeerBillEntity b) {
        this(b.getRed(), b.getBlue(), b.getWhite(), b.getGreen(), GREEN_VALUE_DEFAULT, GREEN_VALUE_DEFAULT_TEXT, b.getValue(), b.getDate(), b.getDescr());
    }

    public BeerBillEntity toBillEntity() {
        return new BeerBillEntity(red, blue, white, green, value, date, descr);
    }

    public String formattedDate() {
        return FormatUtils.formatDateToDisplay(date);
    }

    public String formattedValue() {
        return FormatUtils.formatDoubleToEuro(value);
    }

    public boolean isConsumptionBill(){
        return red > 0 || blue > 0 || white > 0 || green > 0;
    }

    @Override
    public int compareTo(BeerBillDTO o) {
        return o.date.compareTo(date);
    }
}

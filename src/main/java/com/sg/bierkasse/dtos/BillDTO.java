package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.BillEntity;
import com.sg.bierkasse.utils.helpers.FormatUtils;

import java.util.Date;

public record BillDTO(
        int red,
        int blue,
        int white,
        int green,
        double greenValue,
        String greenText,
        double value,
        Date date,
        String descr) implements Comparable<BillDTO> {

    public static final double BLUE_VALUE = 0.5;
    public static final double RED_VALUE = 1;
    public static final double WHITE_VALUE = 1.1;
    public static final double GREEN_VALUE_DEFAULT = 5;
    public static final String GREEN_VALUE_DEFAULT_TEXT = "Ring Grün";

    public BillDTO(BillEntity b) {
        this(b.getRed(), b.getBlue(), b.getWhite(), b.getGreen(), GREEN_VALUE_DEFAULT, GREEN_VALUE_DEFAULT_TEXT, b.getValue(), b.getDate(), b.getDescr());
    }

    public BillEntity toBillEntity() {
        return new BillEntity(red, blue, white, green, value, date, descr);
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
    public int compareTo(BillDTO o) {
        return o.date.compareTo(date);
    }
}

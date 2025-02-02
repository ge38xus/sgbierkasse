package com.sg.bierkasse.utils.helpers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.GERMAN);

    public static String formatDateToDisplay(Date date) {
        if (date == null) {
            return " - ";
        }
        return simpleDateFormat.format(date);
    }

    public static String formatDoubleToEuro(double number) {
        if (number == 0) {
            return "-";
        }
        return df.format(number) + " €";
    }
}

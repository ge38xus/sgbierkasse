package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.RechnungEntity;
import com.sg.bierkasse.utils.helpers.FormatUtils;

import java.util.Date;

public record RechnungDTO(
        double value,
        boolean privateMoneyUsed,
        Date date,
        String description
) {


    public RechnungDTO(RechnungEntity p) {
        this(p.getValue(), p.isPrivateMoneyUsed(), p.getDate(), p.getDescription());
    }

    public RechnungEntity toRechnungEntity() {
        return new RechnungEntity(value, privateMoneyUsed, date, description);
    }

    public String formattedDate() {
        return FormatUtils.formatDateToDisplay(date);
    }

    public String formattedValue() {
        return FormatUtils.formatDoubleToEuro(value);
    }
}
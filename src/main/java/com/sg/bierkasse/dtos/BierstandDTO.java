package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.BierstandEntity;
import com.sg.bierkasse.utils.helpers.FormatUtils;
import org.bson.types.ObjectId;

import java.util.Date;

public record BierstandDTO(
     ObjectId id,
     Date date,
     int roteKisten,
     int blaueKisten,
     int weisseKisten,
     double wein,
     double sonstiges,
     double sum,
     double sumSpenden,
     double sumGuthaben,
     double sumSchulden,
     double kassenStand
) {

    public BierstandDTO(BierstandEntity bierstandEntity) {
        this(bierstandEntity.getId(), bierstandEntity.getDate(), bierstandEntity.getRoteKisten(), bierstandEntity.getBlaueKisten(), bierstandEntity.getWeisseKisten(), bierstandEntity.getWein(), bierstandEntity.getSonstiges(), bierstandEntity.getSum(), bierstandEntity.getSumSpenden(), bierstandEntity.getSumGuthaben(), bierstandEntity.getSumSchulden(), bierstandEntity.getKassenStand());
    }

    public String formattedDate() {
        return FormatUtils.formatDateToDisplay(date);
    }

    public String formattedSum() {
        return FormatUtils.formatDoubleToEuro(sum);
    }

    public String formattedKassenStand() {
        return FormatUtils.formatDoubleToEuro(kassenStand);
    }

    public String formattedWein() {
        return FormatUtils.formatDoubleToEuro(wein);
    }

    public String formattedSpenden() {
        return FormatUtils.formatDoubleToEuro(sumSpenden);
    }

    public String formattedGuthaben() {
        return FormatUtils.formatDoubleToEuro(sumGuthaben);
    }

    public String formattedSchulden() {
        return FormatUtils.formatDoubleToEuro(sumSchulden);
    }

    public String formattedRest() {
        return FormatUtils.formatDoubleToEuro(sonstiges);
    }


    public BierstandEntity toBierstandEntity() {
        return new BierstandEntity(id, date, roteKisten, blaueKisten, weisseKisten, wein, sonstiges, sum, sumSpenden, sumGuthaben, sumSchulden, kassenStand);
    }
}

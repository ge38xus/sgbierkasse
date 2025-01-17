package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.BierstandEntity;
import com.sg.bierkasse.utils.Utils;
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
        return Utils.formatDateToDisplay(date);
    }

    public String formattedSum() {
        return Utils.formatDoubleToEuro(sum);
    }

    public String formattedKassenStand() {
        return Utils.formatDoubleToEuro(kassenStand);
    }

    public String formattedWein() {
        return Utils.formatDoubleToEuro(wein);
    }

    public String formattedSpenden() {
        return Utils.formatDoubleToEuro(sumSpenden);
    }

    public String formattedGuthaben() {
        return Utils.formatDoubleToEuro(sumGuthaben);
    }

    public String formattedSchulden() {
        return Utils.formatDoubleToEuro(sumSchulden);
    }

    public String formattedRest() {
        return Utils.formatDoubleToEuro(sonstiges);
    }


    public BierstandEntity toBierstandEntity() {
        return new BierstandEntity(id, date, roteKisten, blaueKisten, weisseKisten, wein, sonstiges, sum, sumSpenden, sumGuthaben, sumSchulden, kassenStand);
    }
}

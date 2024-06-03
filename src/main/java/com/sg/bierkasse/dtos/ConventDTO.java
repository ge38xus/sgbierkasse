package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.ConventEntity;
import com.sg.bierkasse.utils.Utils;
import org.bson.types.ObjectId;

import java.util.Date;

public record ConventDTO(
        ObjectId id,
        Date date,
        String name
) {

    public ConventDTO(ConventEntity conventEntity) {
        this(conventEntity.getId(), conventEntity.getDate(), conventEntity.getName());
    }

    public String formattedDate() {
        return Utils.formatDateToDisplay(date);
    }

    public ConventEntity toConventEntity() {
        return new ConventEntity(id, date, name);
    }
}
package com.sg.bierkasse.entities;

import org.bson.types.ObjectId;

public interface BaseEntity {

    void setId(ObjectId objectId);
    ObjectId getId();
}

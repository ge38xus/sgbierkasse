package com.sg.bierkasse.entities;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class ConventEntity {

    private ObjectId id;
    private Date date;
    private String name;

    public ConventEntity() {
    }

    public ConventEntity(ObjectId id, Date date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConventEntity that = (ConventEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, name);
    }
}
package com.sg.bierkasse.entities.bills;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class CCBillEntity {

    private ObjectId id;
    private double value;
    private Date date;
    private String descr;

    public CCBillEntity() {

    }

    public CCBillEntity(double value, Date date, String descr) {
        this.value = value;
        this.date = date;
        this.descr = descr;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CCBillEntity that = (CCBillEntity) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, date);
    }

    public ObjectId getId() {
        return this.id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CCBill{date=" + date + ", descr=" + descr + "}";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

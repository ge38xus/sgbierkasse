package com.sg.bierkasse.entities;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class BillEntity {

    private ObjectId id;
    private int red;
    private int blue;
    private int white;
    private int green;
    private double value;
    private Date date;
    private String descr;

    public BillEntity() {

    }

    public BillEntity(int red, int blue, int white, int green, double value, Date date, String descr) {
        this.red = red;
        this.blue = blue;
        this.white = white;
        this.green = green;
        this.value = value;
        this.date = date;
        this.descr = descr;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
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
        BillEntity that = (BillEntity) o;
        return red == that.red && blue == that.blue && white == that.white && green == that.green && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, blue, white, green, value);
    }

    public ObjectId getId() {
        return this.id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Bill{red=" + red + ", blue=" + blue + ", white=" + white + ", green=" + green + ", date=" + date + ", descr=" + descr + "}";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

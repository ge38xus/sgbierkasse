package com.sg.bierkasse.entities;

import java.util.Date;
import java.util.Objects;

public class RechnungEntity {

    private double value;
    private boolean privateMoneyUsed;
    private Date date;
    private String description;

    public RechnungEntity() {

    }

    public RechnungEntity(double value, boolean privateMoneyUsed, Date date, String description) {
        this.value = value;
        this.privateMoneyUsed = privateMoneyUsed;
        this.date = date;
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isPrivateMoneyUsed() {
        return privateMoneyUsed;
    }

    public void setPrivateMoneyUsed(boolean privateMoneyUsed) {
        this.privateMoneyUsed = privateMoneyUsed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RechnungEntity{" +
                "value=" + value +
                ", privateMoneyUsed=" + privateMoneyUsed +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RechnungEntity that = (RechnungEntity) o;
        return Double.compare(that.value, value) == 0 && privateMoneyUsed == that.privateMoneyUsed && Objects.equals(date, that.date) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, privateMoneyUsed, date, description);
    }
}
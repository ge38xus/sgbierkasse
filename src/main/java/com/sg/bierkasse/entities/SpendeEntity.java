package com.sg.bierkasse.entities;

import java.util.Date;

public class SpendeEntity {

    private double value;
    private Date date;
    private Date payedOn;
    private String occasion;
    private boolean used;
    private String usedFor;

    public SpendeEntity() {

    }

    public SpendeEntity(double value, Date date, Date payedOn, String occasion, boolean used, String usedFor) {
        this.value = value;
        this.date = date;
        this.payedOn = payedOn;
        this.occasion = occasion;
        this.used = used;
        this.usedFor = usedFor;
    }

    public Date getPayedOn() {
        return payedOn;
    }

    public void setPayedOn(Date payedOn) {
        this.payedOn = payedOn;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getUsedFor() {
        return usedFor;
    }

    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
    }
}

package com.sg.bierkasse.entities;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class BierstandEntity implements BaseEntity{
    private ObjectId id;
    private Date date;
    private int roteKisten;
    private int blaueKisten;
    private int weisseKisten;
    private double wein;
    private double sonstiges;
    private double sum;
    private double sumSpenden;
    private double sumGuthaben;
    private double sumSchulden;
    private double kassenStand;

    public BierstandEntity() {
    }

    public BierstandEntity(ObjectId id, Date date, int roteKisten, int blaueKisten, int weisseKisten, double wein, double sonstiges, double sum, double sumSpenden, double sumGuthaben, double sumSchulden, double kassenStand) {
        this.id = id;
        this.date = date;
        this.roteKisten = roteKisten;
        this.blaueKisten = blaueKisten;
        this.weisseKisten = weisseKisten;
        this.wein = wein;
        this.sonstiges = sonstiges;
        this.sum = sum;
        this.sumSpenden = sumSpenden;
        this.sumGuthaben = sumGuthaben;
        this.sumSchulden = sumSchulden;
        this.kassenStand = kassenStand;
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

    public int getRoteKisten() {
        return roteKisten;
    }

    public void setRoteKisten(int roteKisten) {
        this.roteKisten = roteKisten;
    }

    public int getBlaueKisten() {
        return blaueKisten;
    }

    public void setBlaueKisten(int blaueKisten) {
        this.blaueKisten = blaueKisten;
    }

    public int getWeisseKisten() {
        return weisseKisten;
    }

    public void setWeisseKisten(int weisseKisten) {
        this.weisseKisten = weisseKisten;
    }

    public double getWein() {
        return wein;
    }

    public void setWein(double wein) {
        this.wein = wein;
    }

    public double getSonstiges() {
        return sonstiges;
    }

    public void setSonstiges(double sonstiges) {
        this.sonstiges = sonstiges;
    }

    public double getKassenStand() {
        return kassenStand;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setKassenStand(double kassenStand) {
        this.kassenStand = kassenStand;
    }

    public double getSum() {
        return sum;
    }

    public double getSumSpenden() {
        return sumSpenden;
    }

    public void setSumSpenden(double sumSpenden) {
        this.sumSpenden = sumSpenden;
    }

    public double getSumGuthaben() {
        return sumGuthaben;
    }

    public void setSumGuthaben(double sumGuthaben) {
        this.sumGuthaben = sumGuthaben;
    }

    public double getSumSchulden() {
        return sumSchulden;
    }

    public void setSumSchulden(double sumSchulden) {
        this.sumSchulden = sumSchulden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BierstandEntity that = (BierstandEntity) o;
        return roteKisten == that.roteKisten && blaueKisten == that.blaueKisten && weisseKisten == that.weisseKisten && Double.compare(that.wein, wein) == 0 && Double.compare(that.sonstiges, sonstiges) == 0 && Double.compare(that.sumSpenden, sumSpenden) == 0 && Double.compare(that.sumGuthaben, sumGuthaben) == 0 && Double.compare(that.sumSchulden, sumSchulden) == 0 && Double.compare(that.sum, sum) == 0 && Double.compare(that.kassenStand, kassenStand) == 0 && Objects.equals(id, that.id) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, roteKisten, blaueKisten, weisseKisten, wein, sonstiges, sumSpenden, sumGuthaben, sumSchulden, sum, kassenStand);
    }
}
package com.sg.bierkasse.entities;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class PersonEntity implements BaseEntity{

    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private String state;
    private List<BillEntity> billEntities;

    private List<RechnungEntity> rechnungEntities;

    private List<SpendeEntity> spendeEntities;

    private boolean excelRelevant;

    private boolean berichtReceiver;

    public PersonEntity() {

    }

    public PersonEntity(ObjectId id,
                        String firstName,
                        String lastName,
                        String email,
                        String state,
                        List<BillEntity> billEntities,
                        List<RechnungEntity> rechnungEntities,
                        List<SpendeEntity> spendeEntities,
                        boolean excelRelevant,
                        boolean berichtReceiver
        ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.state = state;
        this.billEntities = billEntities;
        this.rechnungEntities = rechnungEntities;
        this.spendeEntities = spendeEntities;
        this.excelRelevant = excelRelevant;
        this.berichtReceiver = berichtReceiver;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public PersonEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public PersonEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PersonEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getState() {
        return state;
    }

    public PersonEntity setState(String state) {
        this.state = state;
        return this;
    }

    public List<BillEntity> getBillEntities() {
        return billEntities;
    }

    public PersonEntity setBillEntities(List<BillEntity> billEntities) {
        this.billEntities = billEntities;
        return this;
    }

    public List<RechnungEntity> getRechnungEntities() {
        return rechnungEntities;
    }

    public void setRechnungEntities(List<RechnungEntity> rechnungEntities) {
        this.rechnungEntities = rechnungEntities;
    }

    public boolean isExcelRelevant() {
        return excelRelevant;
    }

    public void setExcelRelevant(boolean excelRelevant) {
        this.excelRelevant = excelRelevant;
    }

    public List<SpendeEntity> getSpendeEntities() {
        return spendeEntities;
    }

    public void setSpendeEntities(List<SpendeEntity> spendeEntities) {
        this.spendeEntities = spendeEntities;
    }

    public boolean isBerichtReceiver() {
        return berichtReceiver;
    }

    public void setBerichtReceiver(boolean berichtReceiver) {
        this.berichtReceiver = berichtReceiver;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email=" + '\'' + email + '\'' + ", state=" + '\'' + state + '\'' + ", bills=" + billEntities + ", excelRelevant=" + excelRelevant + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return excelRelevant == that.excelRelevant &&
                berichtReceiver == that.berichtReceiver &&
                Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(state, that.state) &&
                Objects.equals(billEntities, that.billEntities) &&
                Objects.equals(rechnungEntities, that.rechnungEntities) &&
                Objects.equals(spendeEntities, that.spendeEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                firstName,
                lastName,
                email,
                state,
                billEntities,
                rechnungEntities,
                spendeEntities,
                excelRelevant,
                berichtReceiver);
    }
}
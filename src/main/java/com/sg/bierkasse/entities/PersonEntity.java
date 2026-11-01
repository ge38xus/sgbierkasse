package com.sg.bierkasse.entities;

import com.sg.bierkasse.entities.bills.BeerBillEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PersonEntity implements BaseEntity{

    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private String state;
    private List<BeerBillEntity> billEntities;

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
                        List<BeerBillEntity> billEntities,
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
}
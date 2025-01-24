package com.sg.bierkasse.repositories;

import com.mongodb.client.MongoClient;
import com.sg.bierkasse.entities.BillEntity;
import com.sg.bierkasse.entities.PersonEntity;
import com.sg.bierkasse.entities.RechnungEntity;
import com.sg.bierkasse.entities.SpendeEntity;
import jakarta.annotation.PostConstruct;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.set;

@Repository
public class MongoDBPersonRepo extends MongoDBBaseRepo<PersonEntity> {

    public static final String COLLECTION_NAME = "data";

    public MongoDBPersonRepo(MongoClient mongoClient) {
        super(mongoClient);
    }

    @PostConstruct
    void init() {
        init(COLLECTION_NAME, PersonEntity.class);
    }

    public void pushBill(PersonEntity personEntity, BillEntity billEntity) {
        Bson filter = eq("_id", personEntity.getId());
        Bson change = push("billEntities", billEntity);
        mongoCollection.updateOne(filter, change);
    }

    public void pushRechnung(PersonEntity personEntity, RechnungEntity rechnungEntity) {
        Bson filter = eq("_id", personEntity.getId());
        Bson change = push("rechnungEntities", rechnungEntity);
        mongoCollection.updateOne(filter, change);
    }

    public void pushSpende(PersonEntity personEntity, SpendeEntity spendeEntity) {
        Bson filter = eq("_id", personEntity.getId());
        Bson change = push("spendeEntities", spendeEntity);
        mongoCollection.updateOne(filter, change);
    }

    public void paySpende(SpendeEntity spendeEntity) {
        Bson filter = eq("data.spendeEntities.date", spendeEntity.getDate());
        Bson change = set("data.spendeEntities.payedOn", spendeEntity.getPayedOn());
        mongoCollection.updateOne(filter, change);
    }
}
package com.sg.bierkasse.repositories;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.sg.bierkasse.entities.BillEntity;
import com.sg.bierkasse.entities.PersonEntity;
import com.sg.bierkasse.entities.RechnungEntity;
import com.sg.bierkasse.entities.SpendeEntity;
import jakarta.annotation.PostConstruct;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.ReturnDocument.AFTER;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.set;

@Repository
public class MongoDBPersonRepo implements EntityRepo<PersonEntity> {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
                                                                           .readPreference(ReadPreference.primary())
                                                                           .readConcern(ReadConcern.MAJORITY)
                                                                           .writeConcern(WriteConcern.MAJORITY)
                                                                           .build();
    private final MongoClient client;
    private MongoCollection<PersonEntity> personCollection;

    public MongoDBPersonRepo(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    @PostConstruct
    void init() {
        personCollection = client.getDatabase("test").getCollection("data", PersonEntity.class);
    }

    @Override
    public PersonEntity save(PersonEntity personEntity) {
        personEntity.setId(new ObjectId());
        personCollection.insertOne(personEntity);
        return personEntity;
    }

    @Override
    public List<PersonEntity> saveAll(List<PersonEntity> personEntities) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                personEntities.forEach(p -> p.setId(new ObjectId()));
                personCollection.insertMany(clientSession, personEntities);
                return personEntities;
            }, txnOptions);
        }
    }

    @Override
    public List<PersonEntity> findAll() {
        return personCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<PersonEntity> findAll(List<String> ids) {
        return personCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    @Override
    public PersonEntity findOne(String id) {
        return personCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long count() {
        return personCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return personCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> personCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> personCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public PersonEntity update(PersonEntity personEntity) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return personCollection.findOneAndReplace(eq("_id", personEntity.getId()), personEntity, options);
    }

    @Override
    public long update(List<PersonEntity> personEntities) {
        List<ReplaceOneModel<PersonEntity>> writes = personEntities.stream()
                                                                   .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                                                                                                   p))
                                                                   .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> personCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }


    public void pushBill(PersonEntity personEntity, BillEntity billEntity) {
        Bson filter = eq("_id", personEntity.getId());
        Bson change = push("billEntities", billEntity);
        personCollection.updateOne(filter, change);
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }

    public void pushRechnung(PersonEntity personEntity, RechnungEntity rechnungEntity) {
        Bson filter = eq("_id", personEntity.getId());
        Bson change = push("rechnungEntities", rechnungEntity);
        personCollection.updateOne(filter, change);
    }

    public void pushSpende(PersonEntity personEntity, SpendeEntity spendeEntity) {
        Bson filter = eq("_id", personEntity.getId());
        Bson change = push("spendeEntities", spendeEntity);
        personCollection.updateOne(filter, change);
    }

    public void paySpende(SpendeEntity spendeEntity) {
        Bson filter = eq("data.spendeEntities.date", spendeEntity.getDate());
        Bson change = set("data.spendeEntities.payedOn", spendeEntity.getPayedOn());
        personCollection.updateOne(filter, change);
    }
}
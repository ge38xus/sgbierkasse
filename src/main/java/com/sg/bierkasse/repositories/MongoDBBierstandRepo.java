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
import com.sg.bierkasse.dtos.BierstandDTO;
import com.sg.bierkasse.entities.BierstandEntity;
import com.sg.bierkasse.entities.ConventEntity;
import jakarta.annotation.PostConstruct;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.ReturnDocument.AFTER;

@Repository
public class MongoDBBierstandRepo implements EntityRepo<BierstandEntity> {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
                                                                           .readPreference(ReadPreference.primary())
                                                                           .readConcern(ReadConcern.MAJORITY)
                                                                           .writeConcern(WriteConcern.MAJORITY)
                                                                           .build();
    private final MongoClient client;
    private MongoCollection<BierstandEntity> bierstandCollection;

    public MongoDBBierstandRepo(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    @PostConstruct
    void init() {
        bierstandCollection = client.getDatabase("test").getCollection("bierstand", BierstandEntity.class);
    }

    @Override
    public BierstandEntity save(BierstandEntity conventEntity) {
        conventEntity.setId(new ObjectId());
        bierstandCollection.insertOne(conventEntity);
        return conventEntity;
    }

    @Override
    public List<BierstandEntity> saveAll(List<BierstandEntity> conventEntities) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                conventEntities.forEach(p -> p.setId(new ObjectId()));
                bierstandCollection.insertMany(clientSession, conventEntities);
                return conventEntities;
            }, txnOptions);
        }
    }

    @Override
    public List<BierstandEntity> findAll() {
        return bierstandCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<BierstandEntity> findAll(List<String> ids) {
        return bierstandCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    @Override
    public BierstandEntity findOne(String id) {
        return bierstandCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long count() {
        return bierstandCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return bierstandCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> bierstandCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> bierstandCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public BierstandEntity update(BierstandEntity conventEntity) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return bierstandCollection.findOneAndReplace(eq("_id", conventEntity.getId()), conventEntity, options);
    }

    @Override
    public long update(List<BierstandEntity> conventEntities) {
        List<ReplaceOneModel<BierstandEntity>> writes = conventEntities.stream()
                                                                   .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                                                                                                   p))
                                                                   .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> bierstandCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }
}
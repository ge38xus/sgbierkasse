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
import com.sg.bierkasse.entities.BaseEntity;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.ReturnDocument.AFTER;

public abstract class MongoDBBaseRepo<T extends BaseEntity> implements EntityRepo<T> {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();
    private final MongoClient client;
    MongoCollection<T> mongoCollection;

    public MongoDBBaseRepo(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    void init(String databaseName, String collectionName,  Class<T> aClass) {
        mongoCollection = client.getDatabase(databaseName).getCollection(collectionName, aClass);
    }
    public T save(T entity) {
        entity.setId(new ObjectId());
        mongoCollection.insertOne(entity);
        return entity;
    }

    public List<T> saveAll(List<T> entities) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                entities.forEach(p -> p.setId(new ObjectId()));
                mongoCollection.insertMany(clientSession, entities);
                return entities;
            }, txnOptions);
        }
    }

    @Override
    public List<T> findAll() {
        return mongoCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<T> findAll(List<String> ids) {
        return mongoCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    @Override
    public T findOne(String id) {
        return mongoCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long count() {
        return mongoCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return mongoCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> mongoCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> mongoCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    public T update(T entity) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return mongoCollection.findOneAndReplace(eq("_id", entity.getId()), entity, options);
    }

    @Override
    public long update(List<T> entities) {
        List<ReplaceOneModel<T>> writes = entities.stream()
                .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                        p))
                .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> mongoCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }
}

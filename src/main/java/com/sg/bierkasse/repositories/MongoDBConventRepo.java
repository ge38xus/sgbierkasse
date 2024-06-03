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
public class MongoDBConventRepo implements EntityRepo<ConventEntity> {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
                                                                           .readPreference(ReadPreference.primary())
                                                                           .readConcern(ReadConcern.MAJORITY)
                                                                           .writeConcern(WriteConcern.MAJORITY)
                                                                           .build();
    private final MongoClient client;
    private MongoCollection<ConventEntity> conventCollection;

    public MongoDBConventRepo(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    @PostConstruct
    void init() {
        conventCollection = client.getDatabase("test").getCollection("convente", ConventEntity.class);
    }

    @Override
    public ConventEntity save(ConventEntity conventEntity) {
        conventEntity.setId(new ObjectId());
        conventCollection.insertOne(conventEntity);
        return conventEntity;
    }

    @Override
    public List<ConventEntity> saveAll(List<ConventEntity> conventEntities) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                conventEntities.forEach(p -> p.setId(new ObjectId()));
                conventCollection.insertMany(clientSession, conventEntities);
                return conventEntities;
            }, txnOptions);
        }
    }

    @Override
    public List<ConventEntity> findAll() {
        return conventCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<ConventEntity> findAll(List<String> ids) {
        return conventCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    @Override
    public ConventEntity findOne(String id) {
        return conventCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long count() {
        return conventCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return conventCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> conventCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> conventCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public ConventEntity update(ConventEntity conventEntity) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return conventCollection.findOneAndReplace(eq("_id", conventEntity.getId()), conventEntity, options);
    }

    @Override
    public long update(List<ConventEntity> conventEntities) {
        List<ReplaceOneModel<ConventEntity>> writes = conventEntities.stream()
                                                                   .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                                                                                                   p))
                                                                   .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> conventCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }
}
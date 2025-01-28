package com.sg.bierkasse.repositories;

import com.mongodb.client.MongoClient;

import com.sg.bierkasse.entities.ConventEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDBConventRepo extends MongoDBBaseRepo<ConventEntity> {

    public static final String COLLECTION_NAME = "convente";
    private static final String DATABASE_NAME = "test";

    public MongoDBConventRepo(MongoClient mongoClient) {
        super(mongoClient);
    }

    @PostConstruct
    void init() {
        init(DATABASE_NAME, COLLECTION_NAME, ConventEntity.class);
    }
}
package com.sg.bierkasse.repositories;

import com.mongodb.client.MongoClient;

import com.sg.bierkasse.entities.ConventEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDBConventRepo extends MongoDBBaseRepo<ConventEntity> {

    public static final String COLLECTION_NAME = "convente";

    public MongoDBConventRepo(MongoClient mongoClient) {
        super(mongoClient);
    }

    @PostConstruct
    void init() {
        init(COLLECTION_NAME, ConventEntity.class);
    }
}
package com.sg.bierkasse.repositories;

import com.mongodb.client.MongoClient;
import com.sg.bierkasse.entities.BierstandEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDBBierstandRepo extends MongoDBBaseRepo<BierstandEntity> {

    private final static String COLLECTION_NAME = "bierstand";
    private static final String DATABASE_NAME = "test";


    public MongoDBBierstandRepo(MongoClient mongoClient) {
        super(mongoClient);
    }

    @PostConstruct
    void init() {
        init(DATABASE_NAME, COLLECTION_NAME, BierstandEntity.class);
    }
}
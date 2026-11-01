package com.sg.bierkasse.repositories;

import com.mongodb.client.MongoClient;
import com.sg.bierkasse.entities.UserEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class MongoDBUserRepo extends MongoDBBaseRepo<UserEntity> {

    public static final String COLLECTION_NAME = "users";
    private static final String DATABASE_NAME = "test";


    public MongoDBUserRepo(MongoClient mongoClient) {
        super(mongoClient);
    }

    @PostConstruct
    void init() {
        init(DATABASE_NAME, COLLECTION_NAME, UserEntity.class);
    }

    public Optional<UserEntity> findByUsername(String username) {
        UserEntity user = mongoCollection
                .find(eq("username", username))
                .first();

        return Optional.ofNullable(user);
    }
}
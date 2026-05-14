package com.example.demo700.Security;

import com.example.demo700.Model.UserActiveModel.UserActive;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

import java.util.concurrent.TimeUnit;

@Configuration
public class UserActiveMongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void ensureTTLIndex() {
        IndexOperations indexOps = mongoTemplate.indexOps(UserActive.class);
        
        // Drop existing index if needed (optional)
        // indexOps.dropIndex("lastActivity_ttl");
        
        // Create TTL index properly
        indexOps.ensureIndex(new Index()
            .on("lastActivity", org.springframework.data.domain.Sort.Direction.ASC)
            .expire(60, TimeUnit.SECONDS)
            .named("lastActivity_ttl"));
        
        System.out.println("TTL index ensured on UserActive.lastActivity");
    }
}

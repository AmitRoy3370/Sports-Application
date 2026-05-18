package com.example.demo700.Security;

import com.example.demo700.Models.AthleteLocation.AthleteLocation;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
public class AthleteLocationMongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void setupIndexes() {
        createTTLIndex();
        createLocationIndex();
        createGeoIndex();  // For geospatial queries
    }

    /**
     * Create TTL index for automatic cleanup after 60 seconds
     */
    private void createTTLIndex() {
        IndexOperations indexOps = mongoTemplate.indexOps(AthleteLocation.class);
        
        // Create TTL index on lastActivity field
        indexOps.ensureIndex(new Index()
            .on("lastActivity", org.springframework.data.domain.Sort.Direction.ASC)
            .expire(60, TimeUnit.SECONDS)
            .named("lastActivity_ttl"));
        
        System.out.println("TTL index created/verified for AthleteLocation.lastActivity");
    }

    /**
     * Create compound index for athleteId + locationName for faster queries
     */
    private void createLocationIndex() {
        IndexOperations indexOps = mongoTemplate.indexOps(AthleteLocation.class);
        
        indexOps.ensureIndex(new Index()
            .on("athleteId", org.springframework.data.domain.Sort.Direction.ASC)
            .on("locationName", org.springframework.data.domain.Sort.Direction.ASC)
            .named("athlete_location_idx"));
        
        System.out.println("Compound index created for AthleteLocation");
    }

    /**
     * Create 2dsphere index for geospatial queries
     */
    private void createGeoIndex() {
        IndexOperations indexOps = mongoTemplate.indexOps(AthleteLocation.class);
        
        try {
            // Create 2dsphere index for location-based queries
            indexOps.ensureIndex(new Index()
                .on("lattitude", org.springframework.data.domain.Sort.Direction.ASC)
                .on("longitude", org.springframework.data.domain.Sort.Direction.ASC)
                .named("location_2dsphere"));
            
            System.out.println("Geospatial index created for AthleteLocation");
        } catch (Exception e) {
            System.out.println("Geospatial index creation skipped: " + e.getMessage());
        }
    }

    /**
     * Manual cleanup method - fallback if TTL doesn't work
     */
    public long cleanupExpiredLocations() {
        Date expiryTime = new Date(System.currentTimeMillis() - 60000); // 60 seconds ago
        Query query = new Query(Criteria.where("lastActivity").lt(expiryTime));
        var expiredDocs = mongoTemplate.findAllAndRemove(query, AthleteLocation.class);
        long count = expiredDocs.size();
        if (count > 0) {
            System.out.println("Cleaned up " + count + " expired athlete locations");
        }
        return count;
    }
}

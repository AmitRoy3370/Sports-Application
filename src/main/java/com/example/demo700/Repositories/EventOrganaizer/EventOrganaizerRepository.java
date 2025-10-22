package com.example.demo700.Repositories.EventOrganaizer;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;

@Repository
public interface EventOrganaizerRepository extends MongoRepository<EventOrganaizer, String> {
	
	EventOrganaizer findByUserId(String userId);
	EventOrganaizer findByOrganaizationName(String organaizationName);
	List<EventOrganaizer> findByMatchesContainingIgnoreCase(String matchId);

}

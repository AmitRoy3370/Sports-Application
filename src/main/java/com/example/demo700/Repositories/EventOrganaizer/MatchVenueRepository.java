package com.example.demo700.Repositories.EventOrganaizer;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.EventOrganaizer.MatchVenue;

@Repository
public interface MatchVenueRepository extends MongoRepository<MatchVenue, String> {
	
	List<MatchVenue> findByVenueId(String venueId);
	MatchVenue findByMatchId(String matchId);

}

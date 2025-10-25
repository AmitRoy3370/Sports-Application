package com.example.demo700.Services.EventOrganaizer;

import java.util.List;

import com.example.demo700.Models.EventOrganaizer.MatchVenue;

public interface MatchVenueService {
	
	MatchVenue addMatchVenue(MatchVenue matchVenue, String userId);
	List<MatchVenue> seeAllMatchVenue();
	MatchVenue updateMatchVenue(MatchVenue matchVenue, String userId, String matchVenueId);
	List<MatchVenue> findByVenueId(String venueId);
	MatchVenue findByMatchId(String matchId);
	boolean deleteMatchVenue(String matchVenueId, String userId);

}

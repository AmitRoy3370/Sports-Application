package com.example.demo700.Services.EventOrganaizer;

import java.util.List;

import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;

public interface EventOrganaizerService {
	
	public EventOrganaizer addEventOrganaizer(EventOrganaizer eventOrganaizer, String userId);
	public List<EventOrganaizer> seeAll();
	public EventOrganaizer findByUserId(String userId);
	public EventOrganaizer findByEventOrganaizerId(String eventOrganaizerId);
	public EventOrganaizer findByOrganaizationName(String organaizationName);
	public List<EventOrganaizer> findByMatchesContainingIgnoreCase(String matchId);
	public EventOrganaizer updateEventOrganaizer(String userId, String eventOrganaizerId, EventOrganaizer eventOrganaizer);
	public boolean deleteEventOrganaizer(String userId, String eventOrganaizerId);

}

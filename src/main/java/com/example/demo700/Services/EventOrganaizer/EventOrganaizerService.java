package com.example.demo700.Services.EventOrganaizer;

import java.util.List;

import com.example.demo700.DTOFiles.EventOrganaizerResponse;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;

public interface EventOrganaizerService {
	
	public EventOrganaizer addEventOrganaizer(EventOrganaizer eventOrganaizer, String userId);
	public List<EventOrganaizerResponse> seeAll();
	public EventOrganaizerResponse findByUserId(String userId);
	public EventOrganaizerResponse findByEventOrganaizerId(String eventOrganaizerId);
	public EventOrganaizerResponse findByOrganaizationName(String organaizationName);
	public List<EventOrganaizerResponse> findByMatchesContainingIgnoreCase(String matchId);
	public EventOrganaizer updateEventOrganaizer(String userId, String eventOrganaizerId, EventOrganaizer eventOrganaizer);
	public boolean deleteEventOrganaizer(String userId, String eventOrganaizerId);

}

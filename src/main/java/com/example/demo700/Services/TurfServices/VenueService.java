package com.example.demo700.Services.TurfServices;

import java.util.List;
import java.util.Optional;

import com.example.demo700.DTOFiles.VenueResponse;
import com.example.demo700.Models.Turf.Venue;

public interface VenueService {
	Venue createVenue(Venue v);

	VenueResponse getVenueById(String id);

	List<VenueResponse> searchByAddress(String q);
	List<VenueResponse> getAllVenue();
	Venue updateVeue(String id, Venue updateVenue);
	boolean removeVenue(String id, String userId);
	VenueResponse findByName(String name);
}

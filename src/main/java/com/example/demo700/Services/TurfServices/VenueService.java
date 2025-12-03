package com.example.demo700.Services.TurfServices;

import java.util.List;
import java.util.Optional;

import com.example.demo700.Models.Turf.Venue;

public interface VenueService {
	Venue createVenue(Venue v);

	Optional<Venue> getVenueById(String id);

	List<Venue> searchByAddress(String q);
	List<Venue> getAllVenue();
	Venue updateVeue(String id, Venue updateVenue);
	boolean removeVenue(String id, String userId);
	Venue findByName(String name);
}

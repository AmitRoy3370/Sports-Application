package com.example.demo700.Services.TurfServices;

import java.util.List;

import com.example.demo700.Models.Turf.VenueLocation;

public interface VenueLocationService {

	VenueLocation addVenueLocation(VenueLocation venueLocation, String userId);
	List<VenueLocation> seeAllVenueLocation();
	VenueLocation searchVenueLocation(String venueLocationId);
	List<VenueLocation> searchVenueLocationByName(String locationName);
	VenueLocation updateVenueLocation(String venueLocationId, VenueLocation updatedVenueLocation, String userId);
	boolean deleteVenueLocation(String venueLocationId, String userId);
	
}

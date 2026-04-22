package com.example.demo700.Services.TurfServices;

import java.util.List;

import com.example.demo700.DTOFiles.VenueLocationResponse;
import com.example.demo700.Models.Turf.VenueLocation;

public interface VenueLocationService {

	VenueLocation addVenueLocation(VenueLocation venueLocation, String userId);
	List<VenueLocationResponse> seeAllVenueLocation();
	VenueLocationResponse searchVenueLocation(String venueLocationId);
	List<VenueLocationResponse> searchVenueLocationByName(String locationName);
	VenueLocation updateVenueLocation(String venueLocationId, VenueLocation updatedVenueLocation, String userId);
	boolean deleteVenueLocation(String venueLocationId, String userId);
	
}

package com.example.demo700.Repositories.Turf;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo700.Models.Turf.VenueLocation;

public interface VenueLocationServiceRepository extends MongoRepository<VenueLocation, String> {
	
	List<VenueLocation> findByLocationName(String locationName);
	VenueLocation findByLatitudeAndLongitude(double latitude, double longitude);
	VenueLocation findByVenueId(String venueId);

}

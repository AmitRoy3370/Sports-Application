package com.example.demo700.Models.Turf;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueFilesRepository extends MongoRepository<VenueImages, String> {

	public VenueImages findByVenueId(String venueId);
	public List<VenueImages> findByVenueFilesContainingIgnoreCase(String fileId);
	
}

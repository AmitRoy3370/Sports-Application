package com.example.demo700.Repositories.Turf;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.Venue;

@Repository
public interface VenueRepository extends MongoRepository<Venue, String> {

	List<Venue> findByAddressContainingIgnoreCase(String address);
	List<Venue> findByOwnerId(String ownerId);

}

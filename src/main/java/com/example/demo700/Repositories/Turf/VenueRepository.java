package com.example.demo700.Repositories.Turf;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.Venue;

@Repository
public interface VenueRepository extends MongoRepository<Venue, String> {

	List<Venue> findByAddressContainingIgnoreCase(String address);

	Page<Venue> findByAddressContainingIgnoreCase(String address, Pageable pageable);

	List<Venue> findByOwnerId(String ownerId);

	Page<Venue> findByOwnerId(String ownerId, Pageable pageable);

	Page<Venue> findByOwnerIdIn(List<String> ownersId, Pageable pageable);

	List<Venue> findByOwnerIdIn(List<String> ownersId);

	@Query("{ 'name': { $regex: '^?0$', $options: 'i' } }")
	Venue findByNameIgnoreCase(String name);

	List<Venue> findByNameContainingIgnoreCase(String name);

	@Query("{ 'name': { $regex: '^?0$', $options: 'i' } }")
	Page<Venue> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
	 Page<Venue> findAll(Pageable pageable);

}

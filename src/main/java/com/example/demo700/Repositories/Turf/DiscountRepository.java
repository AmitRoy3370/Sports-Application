package com.example.demo700.Repositories.Turf;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.Discount;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {
	
	Discount findByCode(String code);
	List<Discount> findByOwnerId(String ownerId);
	List<Discount> findByOwnerIdAndVenueId(String ownerId, String venueId);
	List<Discount> findByVenueId(String venueId);
	
}

package com.example.demo700.Repositories.Turf;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.Owner;

@Repository
public interface OwnerRepository extends MongoRepository<Owner, String>  {
	
	@Query("{ 'name': { $regex: '^?0$', $options: 'i' } }")
	List<Owner> searchByNameIgnoreCase(String name);
	List<Owner> searchByPhone(String phone);
	Owner searchByUserId(String userId);
	
}

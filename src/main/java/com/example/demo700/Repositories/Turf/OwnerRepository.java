package com.example.demo700.Repositories.Turf;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.Owner;

@Repository
public interface OwnerRepository extends MongoRepository<Owner, String>  {
	
	List<Owner> searchByName(String name);
	List<Owner> searchByPhone(String phone);
	Owner searchByUserId(String userId);
	

}

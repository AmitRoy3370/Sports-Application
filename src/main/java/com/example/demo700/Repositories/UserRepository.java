package com.example.demo700.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);

	@Query("{ 'name': { $regex: '^?0$' } }")
	User findByNameIgnoreCase(String userName);
	
}

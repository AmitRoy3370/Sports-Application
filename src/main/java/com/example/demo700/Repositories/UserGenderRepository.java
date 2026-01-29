package com.example.demo700.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.ENUMS.Gender;
import com.example.demo700.Models.UserGender;

@Repository
public interface UserGenderRepository extends MongoRepository<UserGender, String> {

	public UserGender findByUserId(String userId);
	public List<UserGender> findByGender(Gender gender);
	
}

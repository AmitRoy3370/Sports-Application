package com.example.demo700.Repositories.Athelete;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo700.Models.Athlete.Coach;

import java.util.List;

public interface CoachRepository extends MongoRepository<Coach, String> {
	
	Coach findByAtheleteId(String atheleteId);

}

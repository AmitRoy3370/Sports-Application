package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Athlete.Scouts;

@Repository
public interface ScoutsRepository extends MongoRepository<Scouts, String> {
	
	Scouts findByAtheleteId(String atheleteId);
	List<Scouts> findByEventsContainingIgnoreCase(String scoutId);
	List<Scouts> findByMatchesContainingIgnoreCase(String scoutId);

}

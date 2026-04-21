package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.Scouts;

@Repository
public interface ScoutsRepository extends MongoRepository<Scouts, String> {
	
	Scouts findByAtheleteId(String atheleteId);
	
	Page<Scouts> findAll(Pageable pageable);
	
	List<Scouts> findByEventsContainingIgnoreCase(String eventsId);
	Page<Scouts> findByEventsContainingIgnoreCase(String scoutId, Pageable pageable);
	List<Scouts> findByMatchesContainingIgnoreCase(String matchId);
	Page<Scouts> findByMatchesContainingIgnoreCase(String matchId, Pageable pageable);

	List<Scouts> findByAtheleteIdIn(List<String> athleteIds);
	
}

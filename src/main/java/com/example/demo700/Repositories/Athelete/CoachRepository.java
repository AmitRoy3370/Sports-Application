package com.example.demo700.Repositories.Athelete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo700.Models.Athlete.Coach;

import java.util.List;

public interface CoachRepository extends MongoRepository<Coach, String> {
	
	Coach findByAtheleteId(String atheleteId);

	// New pagination methods
    Page<Coach> findAll(Pageable pageable);
    
    @Query("{ 'atheleteId': { $in: ?0 } }")
    Page<Coach> findByAtheleteIdIn(List<String> athleteIds, Pageable pageable);
    
    @Query("{ 'id': { $in: ?0 } }")
    Page<Coach> findByIdIn(List<String> coachIds, Pageable pageable);
    
    List<Coach> findByAtheleteIdIn(List<String> athleteIds);
	
}

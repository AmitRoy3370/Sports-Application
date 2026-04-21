package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.CoachClassification;

@Repository
public interface CoachClassificationRepository extends MongoRepository<CoachClassification, String> {

	public CoachClassification findByCoachId(String coachId);
	public List<CoachClassification> findByCoachIdIn(List<String> coachIds);
	public Page<CoachClassification> findAll(Pageable pageable);
	public List<CoachClassification> findByCoachClassificationTypes(AthleteClassificationTypes coachClassificationTypes);
	public Page<CoachClassification> findByCoachClassificationTypes(AthleteClassificationTypes coachClassificationTypes, Pageable pageable);
	
}

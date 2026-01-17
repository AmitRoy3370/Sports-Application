package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.AthleteClassification;

@Repository
public interface AthleteClassificationRepository extends MongoRepository<AthleteClassification, String> {

	public AthleteClassification findByAthleteId(String athleteId);
	public List<AthleteClassification> findByAthleteClassificationTypes(AthleteClassificationTypes athleteClassificationTypes);
	
}

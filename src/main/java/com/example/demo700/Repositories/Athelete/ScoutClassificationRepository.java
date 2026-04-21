package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.ScoutClassification;

@Repository
public interface ScoutClassificationRepository extends MongoRepository<ScoutClassification, String> {

	public Page<ScoutClassification> findAll(Pageable pageable);
	public Page<ScoutClassification> findByScoutClassificationTypes(AthleteClassificationTypes scoutClassificationTypes, Pageable pageable);
	public List<ScoutClassification> findByScoutClassificationTypes(AthleteClassificationTypes scoutClassificationTypes);
	
	public ScoutClassification findByScoutId(String scoutId);
	public List<ScoutClassification> findByScoutIdIn(List<String> scoutIds);
	
}

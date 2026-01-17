package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.AthleteClassification;

public interface AthleteClassificationService {

	public AthleteClassification addAthleteClassification(AthleteClassification athleteClassification, String userId);

	public AthleteClassification updateAthleteClassification(AthleteClassification athleteClassification, String userId,
			String athleteClassificationId);

	public List<AthleteClassification> seeAll();

	public AthleteClassification findById(String id);

	public AthleteClassification findByAthleteId(String athleteId);

	public List<AthleteClassification> findByAthleteClassificationTypes(
			AthleteClassificationTypes athleteClassificationTypes);

	public boolean removeAthleteClassification(String id, String userId);
	
}

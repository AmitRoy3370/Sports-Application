package com.example.demo700.Services.Athlete;

import com.example.demo700.DTOFiles.ScoutClassificationListResonseDTO;
import com.example.demo700.DTOFiles.ScoutResponse;
import com.example.demo700.DTOFiles.ScoutsListResponseDTO;
import com.example.demo700.ENUMS.AthleteClassificationTypes;

import com.example.demo700.Models.Athlete.ScoutClassification;

public interface ScoutClassificationService {

	public ScoutClassification addAthleteClassification(ScoutClassification scoutClassification, String userId);

	public ScoutClassification updateAthleteClassification(ScoutClassification scoutClassification, String userId,
			String coachClassificationId);

	public ScoutClassificationListResonseDTO seeAll(int page, int size, String requestedUrl);

	public ScoutResponse findById(String id);

	public ScoutResponse findByCoachId(String coachId);

	public ScoutClassificationListResonseDTO findByAthleteClassificationTypes(
			AthleteClassificationTypes athleteClassificationTypes, int page, int size, String requestUrl);

	public boolean removeAthleteClassification(String id, String userId);
	
	
}

package com.example.demo700.Services.Athlete;


import com.example.demo700.DTOFiles.CoachListResponseDTO;
import com.example.demo700.DTOFiles.CoachResponse;
import com.example.demo700.ENUMS.AthleteClassificationTypes;

import com.example.demo700.Models.Athlete.CoachClassification;

public interface CoachClassificationService {

	public CoachClassification addAthleteClassification(CoachClassification coachClassification, String userId);

	public CoachClassification updateAthleteClassification(CoachClassification coachClassification, String userId,
			String coachClassificationId);

	public CoachListResponseDTO seeAll(int page, int size, String requestedUrl);

	public CoachResponse findById(String id);

	public CoachResponse findByCoachId(String coachId);

	public CoachListResponseDTO findByAthleteClassificationTypes(
			AthleteClassificationTypes athleteClassificationTypes, int page, int size, String requestUrl);

	public boolean removeAthleteClassification(String id, String userId);
	
	
}

package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.AthleteClassification;

public interface AthleteClassificationService {

	public AthleteClassification addAthleteClassification(AthleteClassification athleteClassification, String userId);

	public AthleteClassification updateAthleteClassification(AthleteClassification athleteClassification, String userId,
			String athleteClassificationId);

	public AthleteListResponseDTO seeAll(int page, int size);

	public AthleteRequestDTO findById(String id);

	public AthleteRequestDTO findByAthleteId(String athleteId);

	public AthleteListResponseDTO findByAthleteClassificationTypes(
			AthleteClassificationTypes athleteClassificationTypes, int page, int size);

	public boolean removeAthleteClassification(String id, String userId);
	
}

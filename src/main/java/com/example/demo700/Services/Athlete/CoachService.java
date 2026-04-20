package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.DTOFiles.CoachListResponseDTO;
import com.example.demo700.DTOFiles.CoachResponse;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.Coach;

public interface CoachService {

	Coach addCoach(Coach coach, String userId);
	List<CoachResponse> seeAll();
	CoachListResponseDTO seeAllPaginated(int page, int size, String requestUrl);
	Coach updateCoach(Coach coach, String userID, String coachId);
	boolean deleteCoach(String coachId, String userId);
	CoachResponse searchCoach(String coachId);
	CoachResponse findByAthleteId(String athleteId);
	CoachResponse findByCoachId(String coachId);
	CoachListResponseDTO searchByTeamName(String teamName, int page, int size, String requestUrl);
	List<CoachResponse> findByCoachClassification(AthleteClassificationTypes athleteClassificationTypes);
	CoachListResponseDTO findByCoachClassificationPaginated(AthleteClassificationTypes athleteClassificationTypes, int page, int size, String requestUrl);
	CoachListResponseDTO searchByCoachName(String name, int page, int size, String requestedUrl);
}

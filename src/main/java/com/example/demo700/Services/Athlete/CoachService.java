package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.Models.Athlete.Coach;

public interface CoachService {

	Coach addCoach(Coach coach, String userId);
	List<Coach> seeAll();
	Coach updateCoach(Coach coach, String userID, String coachId);
	boolean deleteCoach(String coachId, String userId);
	Coach searchCoach(String coachId);
	Coach findByAthleteId(String athleteId);
	Coach findByCoachId(String coachId);
	
}

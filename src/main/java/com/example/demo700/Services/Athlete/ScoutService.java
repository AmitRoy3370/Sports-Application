package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.Models.Athlete.Scouts;

public interface ScoutService {
	
	Scouts addScout(Scouts scout, String userId);
	List<Scouts> seeAllScouts();
	Scouts findByAtheleteId(String atheleteId);
	Scouts findByScoutsId(String scoutId);
	Scouts updateScouts(String scoutId, String userId, Scouts updatedScouts);
	boolean deleteScouts(String scoutId, String userId);
	List<Scouts> findByEventsContainingIgnoreCase(String scoutId);
	List<Scouts> findByMatchesContainingIgnoreCase(String scoutId);

}

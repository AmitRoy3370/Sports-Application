package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.Models.Athlete.Team;

public interface TeamService {

	public Team addTeam(Team team, String userId);
	public List<Team> seeAllTeam();
	public Team updateTeam(Team team, String userId, String teamId);
	public boolean deleteTeam(String teamId, String userId);
	Team findByAtheletesContainingIgnoreCase(String atheleteId);
	Team findByCoachesContainingIgnoreCase(String coachId);
	Team findByScoutsContainingIgnoreCase(String scoutId);
	Team findByDoctorsContainingIgnoreCase(String doctorId);
	List<Team> findByMatchesContainingIgnoreCase(String matchId);
	List<Team> findByTeamOwnerId(String teamOwnerId);
	
}

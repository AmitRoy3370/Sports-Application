package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.DTOFiles.TeamResponseDTO;
import com.example.demo700.Models.Athlete.Team;

public interface TeamService {

	public Team addTeam(Team team, String userId);
	public List<TeamResponseDTO> seeAllTeam();
	public Team updateTeam(Team team, String userId, String teamId);
	public boolean deleteTeam(String teamId, String userId);
	TeamResponseDTO findByAtheletesContainingIgnoreCase(String atheleteId);
	TeamResponseDTO findByCoachesContainingIgnoreCase(String coachId);
	TeamResponseDTO findByScoutsContainingIgnoreCase(String scoutId);
	TeamResponseDTO findByDoctorsContainingIgnoreCase(String doctorId);
	List<TeamResponseDTO> findByMatchesContainingIgnoreCase(String matchId);
	List<TeamResponseDTO> findByTeamOwnerId(String teamOwnerId);
	TeamResponseDTO findByTeamName(String teamName);
	TeamResponseDTO findByTeamId(String teamId);
	
}

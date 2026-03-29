package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.DTOFiles.TeamOwnerResponseDTO;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;

public interface TeamOwnerService {

	TeamOwner addTeamOwner(TeamOwner teamOwner);
	List<TeamOwnerResponseDTO> seeAllTeamOwner();
	TeamOwner updateTeamOwner(TeamOwner teamOwner, String userId);
	TeamOwnerResponseDTO findByAthleteId(String athleteId);
	boolean deleteTeamOwner(String teamOwnerId, String userId);
	List<TeamOwnerResponseDTO> findByAchivementsContainingIgnoreCase(String achivement);
	List<TeamOwnerResponseDTO> findByMatchesContainingIgnoreCase(String matchId);
	public TeamOwnerResponseDTO findByTeamsContainingIgnoreCase(String teamId);
	public TeamOwnerResponseDTO findByTeamOwnerId(String teamOwnerId);
	
}

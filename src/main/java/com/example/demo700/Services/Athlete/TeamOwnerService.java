package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;

public interface TeamOwnerService {

	TeamOwner addTeamOwner(TeamOwner teamOwner);
	List<TeamOwner> seeAllTeamOwner();
	TeamOwner updateTeamOwner(TeamOwner teamOwner, String userId);
	boolean deleteTeamOwner(String teamOwnerId, String userId);
	List<TeamOwner> findByAchivementsContainingIgnoreCase(String achivement);
	List<TeamOwner> findByMatchesContainingIgnoreCase(String matchId);
	public TeamOwner findByTeamsContainingIgnoreCase(String teamId);
	
}

package com.example.demo700.DTOFiles;

import java.util.List;

public class TeamOwnerResponseDTO {

	private String id, athleteId, teamOwnerName;
	private List<String> teamsId;
	private List<String> matchesId;
	private List<String> achivements;

	private List<String> teamsName;
	private List<String> matchesName;

	public TeamOwnerResponseDTO(String id, String athleteId, String teamOwnerName, List<String> teamsId,
			List<String> matchesId, List<String> achivements, List<String> teamsName, List<String> matchesName) {
		super();
		this.id = id;
		this.athleteId = athleteId;
		this.teamOwnerName = teamOwnerName;
		this.teamsId = teamsId;
		this.matchesId = matchesId;
		this.achivements = achivements;
		this.teamsName = teamsName;
		this.matchesName = matchesName;
	}

	public TeamOwnerResponseDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAthleteId() {
		return athleteId;
	}

	public void setAthleteId(String athleteId) {
		this.athleteId = athleteId;
	}

	public String getTeamOwnerName() {
		return teamOwnerName;
	}

	public void setTeamOwnerName(String teamOwnerName) {
		this.teamOwnerName = teamOwnerName;
	}

	public List<String> getTeamsId() {
		return teamsId;
	}

	public void setTeamsId(List<String> teamsId) {
		this.teamsId = teamsId;
	}

	public List<String> getMatchesId() {
		return matchesId;
	}

	public void setMatchesId(List<String> matchesId) {
		this.matchesId = matchesId;
	}

	public List<String> getAchivements() {
		return achivements;
	}

	public void setAchivements(List<String> achivements) {
		this.achivements = achivements;
	}

	public List<String> getTeamsName() {
		return teamsName;
	}

	public void setTeamsName(List<String> teamsName) {
		this.teamsName = teamsName;
	}

	public List<String> getMatchesName() {
		return matchesName;
	}

	public void setMatchesName(List<String> matchesName) {
		this.matchesName = matchesName;
	}

	@Override
	public String toString() {
		return "TeamOwnerResponseDTO [id=" + id + ", athleteId=" + athleteId + ", teamOwnerName=" + teamOwnerName
				+ ", teamsId=" + teamsId + ", matchesId=" + matchesId + ", achivements=" + achivements + ", teamsName="
				+ teamsName + ", matchesName=" + matchesName + "]";
	}

}

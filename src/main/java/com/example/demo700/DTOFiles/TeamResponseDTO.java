package com.example.demo700.DTOFiles;

import java.util.List;

public class TeamResponseDTO {

	private String id, teamOwnerId, teamName;
	private String teamOwnerName;

	List<String> athletes;
	List<String> coaches;
	List<String> scouts;
	List<String> doctors;
	List<String> matches;

	List<String> athletesName;
	List<String> coachesName;
	List<String> scoutsName;
	List<String> matchesName;
	List<String> doctorsName;

	public TeamResponseDTO(String id, String teamOwnerId, String teamName, String teamOwnerName, List<String> athletes,
			List<String> coaches, List<String> scouts, List<String> doctors, List<String> matches,
			List<String> athletesName, List<String> coachesName, List<String> scoutsName, List<String> matchesName, List<String> doctorsName) {
		super();
		this.id = id;
		this.teamOwnerId = teamOwnerId;
		this.teamName = teamName;
		this.teamOwnerName = teamOwnerName;
		this.athletes = athletes;
		this.coaches = coaches;
		this.scouts = scouts;
		this.doctors = doctors;
		this.matches = matches;
		this.athletesName = athletesName;
		this.coachesName = coachesName;
		this.scoutsName = scoutsName;
		this.matchesName = matchesName;
		this.doctorsName = doctorsName;
	}

	public TeamResponseDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeamOwnerId() {
		return teamOwnerId;
	}

	public void setTeamOwnerId(String teamOwnerId) {
		this.teamOwnerId = teamOwnerId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamOwnerName() {
		return teamOwnerName;
	}

	public void setTeamOwnerName(String teamOwnerName) {
		this.teamOwnerName = teamOwnerName;
	}

	public List<String> getAthletes() {
		return athletes;
	}

	public void setAthletes(List<String> athletes) {
		this.athletes = athletes;
	}

	public List<String> getCoaches() {
		return coaches;
	}

	public void setCoaches(List<String> coaches) {
		this.coaches = coaches;
	}

	public List<String> getScouts() {
		return scouts;
	}

	public void setScouts(List<String> scouts) {
		this.scouts = scouts;
	}

	public List<String> getDoctors() {
		return doctors;
	}

	public void setDoctors(List<String> doctors) {
		this.doctors = doctors;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	public List<String> getAthletesName() {
		return athletesName;
	}

	public void setAthletesName(List<String> athletesName) {
		this.athletesName = athletesName;
	}

	public List<String> getCoachesName() {
		return coachesName;
	}

	public void setCoachesName(List<String> coachesName) {
		this.coachesName = coachesName;
	}

	public List<String> getScoutsName() {
		return scoutsName;
	}

	public void setScoutsName(List<String> scoutsName) {
		this.scoutsName = scoutsName;
	}

	public List<String> getMatchesName() {
		return matchesName;
	}

	public void setMatchesName(List<String> matchesName) {
		this.matchesName = matchesName;
	}

	public List<String> getDoctorsName() {
		return doctorsName;
	}

	public void setDoctorsName(List<String> doctorsName) {
		this.doctorsName = doctorsName;
	}

	@Override
	public String toString() {
		return "TeamResponseDTO [id=" + id + ", teamOwnerId=" + teamOwnerId + ", teamName=" + teamName
				+ ", teamOwnerName=" + teamOwnerName + ", athletes=" + athletes + ", coaches=" + coaches + ", scouts="
				+ scouts + ", doctors=" + doctors + ", matches=" + matches + ", athletesName=" + athletesName
				+ ", coachesName=" + coachesName + ", scoutsName=" + scoutsName + ", matchesName=" + matchesName
				+ ", doctorsName=" + doctorsName + "]";
	}

}

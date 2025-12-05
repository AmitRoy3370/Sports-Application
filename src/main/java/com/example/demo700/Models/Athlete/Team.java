package com.example.demo700.Models.Athlete;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.lang.NonNull;

import lombok.Data;

@RestController
@Data
public class Team {

	@Id
	String id;

	@NonNull
	String teamOwnerId;

	@NonNull
	String teamName;

	List<String> atheletes;
	List<String> coaches;
	List<String> scouts;
	List<String> doctors;
	List<String> matches;

	public Team(String teamOwnerId, String teamName, List<String> atheletes, List<String> coaches, List<String> scouts,
			List<String> doctors,List<String> matches) {
		super();
		this.teamOwnerId = teamOwnerId;
		this.teamName = teamName;
		this.atheletes = atheletes;
		this.coaches = coaches;
		this.scouts = scouts;
		this.doctors = doctors;
		this.matches = matches;
	}

	public Team() {
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

	public List<String> getAtheletes() {
		return atheletes;
	}

	public void setAtheletes(List<String> atheletes) {
		this.atheletes = atheletes;
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

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	public List<String> getDoctors() {
		return doctors;
	}

	public void setDoctors(List<String> doctors) {
		this.doctors = doctors;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", teamOwnerId=" + teamOwnerId + ", teamName=" + teamName + ", atheletes=" + atheletes
				+ ", coaches=" + coaches + ", scouts=" + scouts + ", Doctors= "+ doctors + ", matches=" + matches + "]";
	}

}

package com.example.demo700.Models.Athlete;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "TeamOwner")
@Data
public class TeamOwner {

	@Id
	String id;

	@NonNull
	String atheleteId;

	List<String> teams;
	List<String> matches;
	List<String> achivements;

	public TeamOwner(String atheleteId, List<String> teams, List<String> matches, List<String> achivements) {
		super();
		this.atheleteId = atheleteId;
		this.teams = teams;
		this.matches = matches;
		this.achivements = achivements;
	}

	public TeamOwner() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAtheleteId() {
		return atheleteId;
	}

	public void setAtheleteId(String atheleteId) {
		this.atheleteId = atheleteId;
	}

	public List<String> getTeams() {
		return teams;
	}

	public void setTeams(List<String> teams) {
		this.teams = teams;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	
	
	public List<String> getAchivements() {
		return achivements;
	}

	public void setAchivements(List<String> achivements) {
		this.achivements = achivements;
	}

	@Override
	public String toString() {
		return "TeamOwner [id=" + id + ", atheleteId=" + atheleteId + ", teams=" + teams + ", matches=" + matches
				+ ", achivements=" + achivements + "]";
	}

}

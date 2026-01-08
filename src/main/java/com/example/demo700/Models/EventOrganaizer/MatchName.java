package com.example.demo700.Models.EventOrganaizer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "MatchName")
public class MatchName {

	@Id
	private String id;

	@NonNull
	private String name;

	@NonNull
	private String matchId;

	public MatchName(String name, String matchId) {
		super();
		this.name = name;
		this.matchId = matchId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	@Override
	public String toString() {
		return "MatchName [id=" + id + ", name=" + name + ", matchId=" + matchId + "]";
	}

}

package com.example.demo700.Models.EventOrganaizer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "MatchVenue")
@Data
public class MatchVenue {

	@Id
	private String id;

	@NonNull
	private String venueId;

	@NonNull
	private String matchId;

	public MatchVenue(String venueId, String matchId) {
		super();
		this.venueId = venueId;
		this.matchId = matchId;
	}

	public MatchVenue() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	@Override
	public String toString() {
		return "MatchVenue [id=" + id + ", venueId=" + venueId + ", matchId=" + matchId + "]";
	}

}

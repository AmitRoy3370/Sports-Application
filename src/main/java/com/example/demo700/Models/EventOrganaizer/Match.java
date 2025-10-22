package com.example.demo700.Models.EventOrganaizer;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "Matches")
@Data
public class Match {

	@Id
	String id;

	@NonNull
	String organaizerId;

	@NonNull
	List<String> teams;

	List<String> gameLogs;

	List<String> videos;
	
	double price;

	@NonNull
	Instant matchStartTime, matchEndTime;

	public Match(String organaizerId, List<String> teams, List<String> gameLogs, List<String> videos,
			Instant matchEndTime, double price) {
		super();
		this.organaizerId = organaizerId;
		this.teams = teams;
		this.gameLogs = gameLogs;
		this.videos = videos;
		this.matchStartTime = Instant.now();
		this.matchEndTime = matchEndTime;
		this.price = price;
	}

	public Match() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrganaizerId() {
		return organaizerId;
	}

	public void setOrganaizerId(String organaizerId) {
		this.organaizerId = organaizerId;
	}

	public List<String> getTeams() {
		return teams;
	}

	public void setTeams(List<String> teams) {
		this.teams = teams;
	}

	public List<String> getGameLogs() {
		return gameLogs;
	}

	public void setGameLogs(List<String> gameLogs) {
		this.gameLogs = gameLogs;
	}

	public List<String> getVideos() {
		return videos;
	}

	public void setVideos(List<String> videos) {
		this.videos = videos;
	}

	public Instant getMatchStartTime() {
		return matchStartTime;
	}

	public void setMatchStartTime(Instant matchStartTime) {
		this.matchStartTime = matchStartTime;
	}

	public Instant getMatchEndTime() {
		return matchEndTime;
	}

	public void setMatchEndTime(Instant matchEndTime) {
		this.matchEndTime = matchEndTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Match [id=" + id + ", organaizerId=" + organaizerId + ", teams=" + teams + ", gameLogs=" + gameLogs
				+ ", videos=" + videos + ", price=" + price + ", matchStartTime=" + matchStartTime + ", matchEndTime="
				+ matchEndTime + "]";
	}

}

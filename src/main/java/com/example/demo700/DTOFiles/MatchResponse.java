package com.example.demo700.DTOFiles;

import java.time.Instant;
import java.util.List;

public class MatchResponse {

	private String id, organaizerId, organaizerName;
	private String matchName, matchVenue, matchVenueId, matchNameId;
	private List<String> teamsId;
	private List<String> gameLogs;
	private List<String> videos;
	private double matchPrice;
	private List<String> teamNames;

	Instant matchStartTime, matchEndTime;

	public MatchResponse(String id, String organaizerId, String organaizerName, String matchName, String matchVenue,
			String matchVenueId, String matchNameId, List<String> teamsId, List<String> gameLogs, List<String> videos,
			double matchPrice, List<String> teamNames, Instant matchStartTime, Instant matchEndTime) {
		super();
		this.id = id;
		this.organaizerId = organaizerId;
		this.organaizerName = organaizerName;
		this.matchName = matchName;
		this.matchVenue = matchVenue;
		this.matchVenueId = matchVenueId;
		this.matchNameId = matchNameId;
		this.teamsId = teamsId;
		this.gameLogs = gameLogs;
		this.videos = videos;
		this.matchPrice = matchPrice;
		this.teamNames = teamNames;
		this.matchStartTime = matchStartTime;
		this.matchEndTime = matchEndTime;
	}

	public MatchResponse() {
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

	public String getOrganaizerName() {
		return organaizerName;
	}

	public void setOrganaizerName(String organaizerName) {
		this.organaizerName = organaizerName;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public String getMatchVenue() {
		return matchVenue;
	}

	public void setMatchVenue(String matchVenue) {
		this.matchVenue = matchVenue;
	}

	public String getMatchVenueId() {
		return matchVenueId;
	}

	public void setMatchVenueId(String matchVenueId) {
		this.matchVenueId = matchVenueId;
	}

	public String getMatchNameId() {
		return matchNameId;
	}

	public void setMatchNameId(String matchNameId) {
		this.matchNameId = matchNameId;
	}

	public List<String> getTeamsId() {
		return teamsId;
	}

	public void setTeamsId(List<String> teamsId) {
		this.teamsId = teamsId;
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

	public double getMatchPrice() {
		return matchPrice;
	}

	public void setMatchPrice(double matchPrice) {
		this.matchPrice = matchPrice;
	}

	public List<String> getTeamNames() {
		return teamNames;
	}

	public void setTeamNames(List<String> teamNames) {
		this.teamNames = teamNames;
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

	@Override
	public String toString() {
		return "MatchResponse [id=" + id + ", organaizerId=" + organaizerId + ", organaizerName=" + organaizerName
				+ ", matchName=" + matchName + ", matchVenue=" + matchVenue + ", matchVenueId=" + matchVenueId
				+ ", matchNameId=" + matchNameId + ", teamsId=" + teamsId + ", gameLogs=" + gameLogs + ", videos="
				+ videos + ", matchPrice=" + matchPrice + ", teamNames=" + teamNames + ", matchStartTime="
				+ matchStartTime + ", matchEndTime=" + matchEndTime + "]";
	}

}

package com.example.demo700.DTOFiles;

import java.util.List;

public class EventOrganaizerResponse {

	String id;

	String userId, userName;

	String organaizationName;

	List<String> matches;
	
	List<MatchResponse> matchesResponse;

	public EventOrganaizerResponse(String id, String userId, String userName, String organaizationName,
			List<String> matches, List<MatchResponse> matchesResponse) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.organaizationName = organaizationName;
		this.matches = matches;
		this.matchesResponse = matchesResponse;
	}

	public EventOrganaizerResponse() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrganaizationName() {
		return organaizationName;
	}

	public void setOrganaizationName(String organaizationName) {
		this.organaizationName = organaizationName;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	public List<MatchResponse> getMatchesResponse() {
		return matchesResponse;
	}

	public void setMatchesName(List<MatchResponse> matchesName) {
		this.matchesResponse = matchesName;
	}

	@Override
	public String toString() {
		return "EventOrganaizerResponse [id=" + id + ", userId=" + userId + ", userName=" + userName
				+ ", organaizationName=" + organaizationName + ", matches=" + matches + ", matchesResponse=" + matchesResponse
				+ "]";
	}

}

package com.example.demo700.DTOFiles;

import java.util.Set;

public class DoctorResponse {

	String id;

	String userId, userName, teamName;

	Set<String> degress;

	Set<String> workingExperiences;

	int yearOfExperiences;

	String designation;

	public DoctorResponse(String id, String userId, String userName, String teamName, Set<String> degress,
			Set<String> workingExperiences, int yearOfExperiences, String designation) {
		super();
		this.id = id;

		this.userId = userId;
		this.userName = userName;
		this.teamName = teamName;
		this.degress = degress;
		this.workingExperiences = workingExperiences;
		this.yearOfExperiences = yearOfExperiences;
		this.designation = designation;
	}

	public DoctorResponse() {
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Set<String> getDegress() {
		return degress;
	}

	public void setDegress(Set<String> degress) {
		this.degress = degress;
	}

	public Set<String> getWorkingExperiences() {
		return workingExperiences;
	}

	public void setWorkingExperiences(Set<String> workingExperiences) {
		this.workingExperiences = workingExperiences;
	}

	public int getYearOfExperiences() {
		return yearOfExperiences;
	}

	public void setYearOfExperiences(int yearOfExperiences) {
		this.yearOfExperiences = yearOfExperiences;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "DoctorResponse [id=" + id + ", userId=" + userId + ", userName=" + userName + ", teamName=" + teamName
				+ ", degress=" + degress + ", workingExperiences=" + workingExperiences + ", yearOfExperiences="
				+ yearOfExperiences + ", designation=" + designation + "]";
	}

}

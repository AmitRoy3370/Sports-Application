package com.example.demo700.Models.DoctorModels;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "Doctor")
public class Doctor {

	@Id
	String id;

	@NonNull
	String userId;

	Set<String> degress;

	Set<String> workingExperiences;

	int yearOfExperiences;

	String designation;

	public Doctor(String userId, Set<String> degress, Set<String> workingExperiences, int yearOfExperiences,
			String designation) {
		super();
		this.userId = userId;
		this.degress = degress;
		this.workingExperiences = workingExperiences;
		this.yearOfExperiences = yearOfExperiences;
		this.designation = designation;
	}

	public Doctor() {
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
		return "Doctor [id=" + id + ", userId=" + userId + ", degress=" + degress + ", workingExperiences="
				+ workingExperiences + ", yearOfExperiences=" + yearOfExperiences + ", designation=" + designation
				+ "]";
	}

}

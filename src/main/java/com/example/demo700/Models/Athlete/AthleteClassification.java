package com.example.demo700.Models.Athlete;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo700.ENUMS.AthleteClassificationTypes;

import com.mongodb.lang.NonNull;

@Document(collection = "AthleteClassification")
public class AthleteClassification {

	@Id
	private String id;

	@NonNull
	private String athleteId;

	@NonNull
	private AthleteClassificationTypes athleteClassificationTypes;

	public AthleteClassification(String athleteId, AthleteClassificationTypes athleteClassificationTypes) {
		super();
		this.athleteId = athleteId;
		this.athleteClassificationTypes = athleteClassificationTypes;
	}

	public AthleteClassification() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAthleteId() {
		return athleteId;
	}

	public void setAthleteId(String athleteId) {
		this.athleteId = athleteId;
	}

	public AthleteClassificationTypes getAthleteClassificationTypes() {
		return athleteClassificationTypes;
	}

	public void setAthleteClassificationTypes(AthleteClassificationTypes athleteClassificationTypes) {
		this.athleteClassificationTypes = athleteClassificationTypes;
	}

	@Override
	public String toString() {
		return "AthleteClassification [id=" + id + ", athleteId=" + athleteId + ", athleteClassificationTypes="
				+ athleteClassificationTypes + "]";
	}

}

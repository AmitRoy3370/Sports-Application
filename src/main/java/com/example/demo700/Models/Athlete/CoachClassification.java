package com.example.demo700.Models.Athlete;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.mongodb.lang.NonNull;

@Document(collection = "CoachClassification")
public class CoachClassification {

	@Id
	private String id;

	@NonNull
	@Indexed
	private String coachId;

	@NonNull
	@Indexed
	private AthleteClassificationTypes coachClassificationTypes;

	public CoachClassification(String coachId, AthleteClassificationTypes coachClassificationTypes) {
		super();
		this.coachId = coachId;
		this.coachClassificationTypes = coachClassificationTypes;
	}

	public CoachClassification() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoachId() {
		return coachId;
	}

	public void setCoachId(String coachId) {
		this.coachId = coachId;
	}

	public AthleteClassificationTypes getCoachClassificationTypes() {
		return coachClassificationTypes;
	}

	public void setCoachClassificationTypes(AthleteClassificationTypes coachClassificationTypes) {
		this.coachClassificationTypes = coachClassificationTypes;
	}

	@Override
	public String toString() {
		return "CoachClassification [id=" + id + ", coachId=" + coachId + ", coachClassificationTypes="
				+ coachClassificationTypes + "]";
	}

}

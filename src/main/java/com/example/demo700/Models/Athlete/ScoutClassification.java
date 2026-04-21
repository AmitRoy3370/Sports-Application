package com.example.demo700.Models.Athlete;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.mongodb.lang.NonNull;

@Document(collection = "ScoutClassification")
public class ScoutClassification {

	@Id
	private String id;

	@NonNull
	@Indexed
	private String scoutId;

	@NonNull
	@Indexed
	private AthleteClassificationTypes scoutClassificationTypes;

	public ScoutClassification(String scoutId, AthleteClassificationTypes scoutClassificationTypes) {
		super();
		
		this.scoutId = scoutId;
		this.scoutClassificationTypes = scoutClassificationTypes;
	}

	public ScoutClassification() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScoutId() {
		return scoutId;
	}

	public void setScoutId(String scoutId) {
		this.scoutId = scoutId;
	}

	public AthleteClassificationTypes getScoutClassificationTypes() {
		return scoutClassificationTypes;
	}

	public void setScoutClassificationTypes(AthleteClassificationTypes scoutClassificationTypes) {
		this.scoutClassificationTypes = scoutClassificationTypes;
	}

	@Override
	public String toString() {
		return "ScoutClassification [id=" + id + ", scoutId=" + scoutId + ", scoutClassificationTypes="
				+ scoutClassificationTypes + "]";
	}

	
	
}

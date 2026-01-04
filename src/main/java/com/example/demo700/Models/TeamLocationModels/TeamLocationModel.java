package com.example.demo700.Models.TeamLocationModels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "TeamLocation")
public class TeamLocationModel {

	@Id
	private String id;

	@NonNull
	private String teamId;

	@NonNull
	private String locationName;

	@NonNull
	private double latitude, longitude;

	public TeamLocationModel(String teamId, String locationName, double latitude, double longitude) {
		super();
		this.teamId = teamId;
		this.locationName = locationName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public TeamLocationModel() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "TeamLocationModel [id=" + id + ", teamId=" + teamId + ", locationName=" + locationName + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}

}

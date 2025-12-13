package com.example.demo700.Models.AthleteLocation;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "AthleteLocation")
public class AthleteLocation {

	@Id
	private String id;

	@NonNull
	private String athleteId;

	@NonNull
	private String locationName;

	@NonNull
	private double lattitude;

	@NonNull
	private double longitude;

	public AthleteLocation(String userId, String locationName, double lattitude, double longitude) {
		super();
		this.athleteId = userId;
		this.locationName = locationName;
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public AthleteLocation() {
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

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "AthleteLocation [id=" + id + ", athleteId=" + athleteId + ", locationName=" + locationName + ", lattitude="
				+ lattitude + ", longitude=" + longitude + "]";
	}

}

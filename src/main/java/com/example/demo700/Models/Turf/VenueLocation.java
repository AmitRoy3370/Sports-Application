package com.example.demo700.Models.Turf;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "VenueLocation")
@Data
public class VenueLocation {

	@Id
	private String id;
	@NonNull
	private String venueId;
	@NonNull
	private double latitude, longitude;
	@NonNull
	private String locationName;

	public VenueLocation(String venueId, double latitude, double longitude, String locationName) {
		super();
		this.venueId = venueId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationName = locationName;
	}

	public VenueLocation() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
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

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}

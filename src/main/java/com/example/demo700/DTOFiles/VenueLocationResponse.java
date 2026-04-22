package com.example.demo700.DTOFiles;

public class VenueLocationResponse {

	private String id;

	private String venueId, venueName;

	private double latitude, longitude;

	private String locationName;

	public VenueLocationResponse(String id, String venueId, String venueName, double latitude, double longitude,
			String locationName) {
		super();
		this.id = id;
		this.venueId = venueId;
		this.venueName = venueName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationName = locationName;
	}

	public VenueLocationResponse() {
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

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
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

	@Override
	public String toString() {
		return "VenueLocationResponse [id=" + id + ", venueId=" + venueId + ", venueName=" + venueName + ", latitude="
				+ latitude + ", longitude=" + longitude + ", locationName=" + locationName + "]";
	}

}

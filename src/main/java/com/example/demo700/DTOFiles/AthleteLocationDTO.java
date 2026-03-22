package com.example.demo700.DTOFiles;

public class AthleteLocationDTO {

	private String id;

	private String athleteId;

	private String locationName;

	private double lattitude;

	private double longitude;

	private String userName;

	public AthleteLocationDTO(String id, String athleteId, String locationName, double lattitude, double longitude,
			String userName) {
		super();
		this.id = id;
		this.athleteId = athleteId;
		this.locationName = locationName;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.userName = userName;
	}

	public AthleteLocationDTO() {
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}

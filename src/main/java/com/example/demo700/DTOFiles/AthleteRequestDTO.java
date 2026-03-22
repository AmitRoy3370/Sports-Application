package com.example.demo700.DTOFiles;

import java.util.List;
import java.util.Set;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.ENUMS.Gender;
import com.example.demo700.ENUMS.Role;

public class AthleteRequestDTO {

	private String id, userId, name, userGenderId, athleteClassificationId, locationId;
	private String email, imageHex;
	private Gender gender;
	private String position, presentTeam, locationName;
	private double height, weight, lattitude, longitude;
	private int age;
	private List<String> gameLogs;
	private List<String> eventAttendence;
	private List<String> highlightReels;
	private AthleteClassificationTypes athleteClassificationTypes;
	private Set<Role> roles;

	public AthleteRequestDTO() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPresentTeam() {
		return presentTeam;
	}

	public void setPresentTeam(String presentTeam) {
		this.presentTeam = presentTeam;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getGameLogs() {
		return gameLogs;
	}

	public void setGameLogs(List<String> gameLogs) {
		this.gameLogs = gameLogs;
	}

	public List<String> getEventAttendence() {
		return eventAttendence;
	}

	public void setEventAttendence(List<String> eventAttendence) {
		this.eventAttendence = eventAttendence;
	}

	public List<String> getHighlightReels() {
		return highlightReels;
	}

	public void setHighlightReels(List<String> highlightReels) {
		this.highlightReels = highlightReels;
	}

	public AthleteClassificationTypes getAthleteClassificationTypes() {
		return athleteClassificationTypes;
	}

	public void setAthleteClassificationTypes(AthleteClassificationTypes athleteClassificationTypes) {
		this.athleteClassificationTypes = athleteClassificationTypes;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getUserGenderId() {
		return userGenderId;
	}

	public void setUserGenderId(String userGenderId) {
		this.userGenderId = userGenderId;
	}

	public String getAthleteClassificationId() {
		return athleteClassificationId;
	}

	public void setAthleteClassificationId(String athleteClassificationId) {
		this.athleteClassificationId = athleteClassificationId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getImageHex() {
		return imageHex;
	}

	public void setImageHex(String imageHex) {
		this.imageHex = imageHex;
	}

	@Override
	public String toString() {
		return "AthleteRequestDTO [id=" + id + ", userId=" + userId + ", name=" + name + ", userGenderId="
				+ userGenderId + ", athleteClassificationId=" + athleteClassificationId + ", locationId=" + locationId
				+ ", email=" + email + ", imageHex=" + imageHex + ", gender=" + gender + ", position=" + position
				+ ", presentTeam=" + presentTeam + ", locationName=" + locationName + ", height=" + height + ", weight="
				+ weight + ", lattitude=" + lattitude + ", longitude=" + longitude + ", age=" + age + ", gameLogs="
				+ gameLogs + ", eventAttendence=" + eventAttendence + ", highlightReels=" + highlightReels
				+ ", athleteClassificationTypes=" + athleteClassificationTypes + ", roles=" + roles + "]";
	}

}

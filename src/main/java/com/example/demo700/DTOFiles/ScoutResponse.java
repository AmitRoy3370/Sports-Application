package com.example.demo700.DTOFiles;

import java.util.List;
import java.util.Set;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.ENUMS.Gender;
import com.example.demo700.ENUMS.Role;

public class ScoutResponse {

	private String userId, name, userGenderId, athleteClassificationId, locationId;
	private String email, imageHex;
	private Gender gender;
	private String position, presentAthleteTeam, locationName;
	private double height, weight, lattitude, longitude;
	private int age;
	private List<String> gameLogs;
	private List<String> eventAttendence;
	private List<String> eventNames;
	private List<String> highlightReels;
	private AthleteClassificationTypes athleteClassificationTypes;
	private Set<Role> roles;

	private String id;

	private String atheleteId;

	private List<String> scoutsEvents;

	private List<String> scoutsEventNames;

	private List<String> scoutsMatches;

	private List<String> scoutsMatchNames;

	public ScoutResponse(String userId, String name, String userGenderId, String athleteClassificationId,
			String locationId, String email, String imageHex, Gender gender, String position, String presentAthleteTeam,
			String locationName, double height, double weight, double lattitude, double longitude, int age,
			List<String> gameLogs, List<String> eventAttendence, List<String> eventNames, List<String> highlightReels,
			AthleteClassificationTypes athleteClassificationTypes, Set<Role> roles, String id, String atheleteId,
			List<String> scoutsEvents, List<String> scoutsEventNames, List<String> scoutsMatches,
			List<String> scoutsMatchNames) {
		super();
		this.userId = userId;
		this.name = name;
		this.userGenderId = userGenderId;
		this.athleteClassificationId = athleteClassificationId;
		this.locationId = locationId;
		this.email = email;
		this.imageHex = imageHex;
		this.gender = gender;
		this.position = position;
		this.presentAthleteTeam = presentAthleteTeam;
		this.locationName = locationName;
		this.height = height;
		this.weight = weight;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.age = age;
		this.gameLogs = gameLogs;
		this.eventAttendence = eventAttendence;
		this.eventNames = eventNames;
		this.highlightReels = highlightReels;
		this.athleteClassificationTypes = athleteClassificationTypes;
		this.roles = roles;
		this.id = id;
		this.atheleteId = atheleteId;
		this.scoutsEvents = scoutsEvents;
		this.scoutsEventNames = scoutsEventNames;
		this.scoutsMatches = scoutsMatches;
		this.scoutsMatchNames = scoutsMatchNames;
	}

	public ScoutResponse() {
		super();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageHex() {
		return imageHex;
	}

	public void setImageHex(String imageHex) {
		this.imageHex = imageHex;
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

	public String getPresentAthleteTeam() {
		return presentAthleteTeam;
	}

	public void setPresentAthleteTeam(String presentAthleteTeam) {
		this.presentAthleteTeam = presentAthleteTeam;
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

	public List<String> getEventNames() {
		return eventNames;
	}

	public void setEventNames(List<String> eventNames) {
		this.eventNames = eventNames;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAtheleteId() {
		return atheleteId;
	}

	public void setAtheleteId(String atheleteId) {
		this.atheleteId = atheleteId;
	}

	public List<String> getScoutsEvents() {
		return scoutsEvents;
	}

	public void setScoutsEvents(List<String> scoutsEvents) {
		this.scoutsEvents = scoutsEvents;
	}

	public List<String> getScoutsEventNames() {
		return scoutsEventNames;
	}

	public void setScoutsEventNames(List<String> scoutsEventNames) {
		this.scoutsEventNames = scoutsEventNames;
	}

	public List<String> getScoutsMatches() {
		return scoutsMatches;
	}

	public void setScoutsMatches(List<String> scoutsMatches) {
		this.scoutsMatches = scoutsMatches;
	}

	public List<String> getScoutsMatchNames() {
		return scoutsMatchNames;
	}

	public void setScoutsMatchNames(List<String> scoutsMatchNames) {
		this.scoutsMatchNames = scoutsMatchNames;
	}

	@Override
	public String toString() {
		return "ScoutResponse [userId=" + userId + ", name=" + name + ", userGenderId=" + userGenderId
				+ ", athleteClassificationId=" + athleteClassificationId + ", locationId=" + locationId + ", email="
				+ email + ", imageHex=" + imageHex + ", gender=" + gender + ", position=" + position
				+ ", presentAthleteTeam=" + presentAthleteTeam + ", locationName=" + locationName + ", height=" + height
				+ ", weight=" + weight + ", lattitude=" + lattitude + ", longitude=" + longitude + ", age=" + age
				+ ", gameLogs=" + gameLogs + ", eventAttendence=" + eventAttendence + ", eventNames=" + eventNames
				+ ", highlightReels=" + highlightReels + ", athleteClassificationTypes=" + athleteClassificationTypes
				+ ", roles=" + roles + ", id=" + id + ", atheleteId=" + atheleteId + ", scoutsEvents=" + scoutsEvents
				+ ", scoutsEventNames=" + scoutsEventNames + ", scoutsMatches=" + scoutsMatches + ", scoutsMatchNames="
				+ scoutsMatchNames + "]";
	}

}

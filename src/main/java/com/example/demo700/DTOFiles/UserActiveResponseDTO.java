package com.example.demo700.DTOFiles;

import java.util.List;
import java.util.Set;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.ENUMS.Gender;
import com.example.demo700.ENUMS.Role;

public class UserActiveResponseDTO {

	private String id;

	private String userId;

	private boolean active;

	private String userName;

	private String athleteId, name, userGenderId, athleteClassificationId, locationId;
	private String email, imageHex;
	private Gender gender;
	private String position, presentTeam, locationName;
	private double height, weight, lattitude, longitude;
	private int age;
	private List<String> gameLogs;
	private List<String> eventAttendence;
	private List<String> eventNames;
	private List<String> highlightReels;
	private AthleteClassificationTypes athleteClassificationTypes;
	private Set<Role> roles;

	public UserActiveResponseDTO() {
		super();
	}

	public UserActiveResponseDTO(String id, String userId, boolean active, String userName, String athleteId,
			String name, String userGenderId, String athleteClassificationId, String locationId, String email,
			String imageHex, Gender gender, String position, String presentTeam, String locationName, double height,
			double weight, double lattitude, double longitude, int age, List<String> gameLogs,
			List<String> eventAttendence, List<String> eventNames, List<String> highlightReels,
			AthleteClassificationTypes athleteClassificationTypes, Set<Role> roles) {
		super();
		this.id = id;
		this.userId = userId;
		this.active = active;
		this.userName = userName;
		this.athleteId = athleteId;
		this.name = name;
		this.userGenderId = userGenderId;
		this.athleteClassificationId = athleteClassificationId;
		this.locationId = locationId;
		this.email = email;
		this.imageHex = imageHex;
		this.gender = gender;
		this.position = position;
		this.presentTeam = presentTeam;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAthleteId() {
		return athleteId;
	}

	public void setAthleteId(String athleteId) {
		this.athleteId = athleteId;
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

	@Override
	public String toString() {
		return "UserActiveResponseDTO [id=" + id + ", userId=" + userId + ", active=" + active + ", userName="
				+ userName + ", athleteId=" + athleteId + ", name=" + name + ", userGenderId=" + userGenderId
				+ ", athleteClassificationId=" + athleteClassificationId + ", locationId=" + locationId + ", email="
				+ email + ", imageHex=" + imageHex + ", gender=" + gender + ", position=" + position + ", presentTeam="
				+ presentTeam + ", locationName=" + locationName + ", height=" + height + ", weight=" + weight
				+ ", lattitude=" + lattitude + ", longitude=" + longitude + ", age=" + age + ", gameLogs=" + gameLogs
				+ ", eventAttendence=" + eventAttendence + ", eventNames=" + eventNames + ", highlightReels="
				+ highlightReels + ", athleteClassificationTypes=" + athleteClassificationTypes + ", roles=" + roles
				+ "]";
	}

}

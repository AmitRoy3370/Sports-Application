package com.example.demo700.DTOFiles;

import java.time.Instant;
import java.util.List;

public class GymResponse {

	private String id;

	private String gymTrainer, gymTrainerName;// this is basically an userId

	private String gymOwner, gymOwnerName; // this is also an user id

	private String tradeLicenseId;

	private String tinNumber;

	private Instant openingTime;

	private Instant closingTime;

	private String coverImageId; // GridFS / Cloud image id
	private List<String> gymImages; // image ids

	private String gymName;

	private String locationName;

	private double latitude, longtitude;

	private double entryFee, monthlyFee;

	private GymMemberResponse gymMembers;

	public GymResponse(String id, String gymTrainer, String gymTrainerName, String gymOwner, String gymOwnerName,
			String tradeLicenseId, String tinNumber, Instant openingTime, Instant closingTime, String coverImageId,
			List<String> gymImages, String gymName, String locationName, double latitude, double longtitude,
			double entryFee, double monthlyFee, GymMemberResponse gymMembers) {
		super();
		this.id = id;
		this.gymTrainer = gymTrainer;
		this.gymTrainerName = gymTrainerName;
		this.gymOwner = gymOwner;
		this.gymOwnerName = gymOwnerName;
		this.tradeLicenseId = tradeLicenseId;
		this.tinNumber = tinNumber;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.coverImageId = coverImageId;
		this.gymImages = gymImages;
		this.gymName = gymName;
		this.locationName = locationName;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.entryFee = entryFee;
		this.monthlyFee = monthlyFee;
		this.gymMembers = gymMembers;
	}

	public GymResponse() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGymTrainer() {
		return gymTrainer;
	}

	public void setGymTrainer(String gymTrainer) {
		this.gymTrainer = gymTrainer;
	}

	public String getGymTrainerName() {
		return gymTrainerName;
	}

	public void setGymTrainerName(String gymTrainerName) {
		this.gymTrainerName = gymTrainerName;
	}

	public String getGymOwner() {
		return gymOwner;
	}

	public void setGymOwner(String gymOwner) {
		this.gymOwner = gymOwner;
	}

	public String getGymOwnerName() {
		return gymOwnerName;
	}

	public void setGymOwnerName(String gymOwnerName) {
		this.gymOwnerName = gymOwnerName;
	}

	public String getTradeLicenseId() {
		return tradeLicenseId;
	}

	public void setTradeLicenseId(String tradeLicenseId) {
		this.tradeLicenseId = tradeLicenseId;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public Instant getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(Instant openingTime) {
		this.openingTime = openingTime;
	}

	public Instant getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(Instant closingTime) {
		this.closingTime = closingTime;
	}

	public String getCoverImageId() {
		return coverImageId;
	}

	public void setCoverImageId(String coverImageId) {
		this.coverImageId = coverImageId;
	}

	public List<String> getGymImages() {
		return gymImages;
	}

	public void setGymImages(List<String> gymImages) {
		this.gymImages = gymImages;
	}

	public String getGymName() {
		return gymName;
	}

	public void setGymName(String gymName) {
		this.gymName = gymName;
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

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getEntryFee() {
		return entryFee;
	}

	public void setEntryFee(double entryFee) {
		this.entryFee = entryFee;
	}

	public double getMonthlyFee() {
		return monthlyFee;
	}

	public void setMonthlyFee(double monthlyFee) {
		this.monthlyFee = monthlyFee;
	}

	public GymMemberResponse getGymMembers() {
		return gymMembers;
	}

	public void setGymMembers(GymMemberResponse gymMembers) {
		this.gymMembers = gymMembers;
	}

	@Override
	public String toString() {
		return "GymResponse [id=" + id + ", gymTrainer=" + gymTrainer + ", gymTrainerName=" + gymTrainerName
				+ ", gymOwner=" + gymOwner + ", gymOwnerName=" + gymOwnerName + ", tradeLicenseId=" + tradeLicenseId
				+ ", tinNumber=" + tinNumber + ", openingTime=" + openingTime + ", closingTime=" + closingTime
				+ ", coverImageId=" + coverImageId + ", gymImages=" + gymImages + ", gymName=" + gymName
				+ ", locationName=" + locationName + ", latitude=" + latitude + ", longtitude=" + longtitude
				+ ", entryFee=" + entryFee + ", monthlyFee=" + monthlyFee + ", gymMembers=" + gymMembers + "]";
	}

}

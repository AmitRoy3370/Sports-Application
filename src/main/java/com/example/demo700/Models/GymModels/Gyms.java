package com.example.demo700.Models.GymModels;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "Gyms")
public class Gyms {

	@Id
	private String id;

	@NonNull
	private String gymTrainer;// this is basically an userId

	@NonNull
	private String gymOwner; // this is also an user id

	private String tradeLicenseId;
	private String tinNumber;
	
	private Instant openingTime, closingTime;

	private String coverImageId;     // GridFS / Cloud image id
    private List<String> gymImages;  // image ids

	@NonNull
	private String gymName;

	@NonNull
	private String locationName;

	@NonNull
	private double latitude, longtitude;

	@NonNull
	private double entryFee, monthlyFee;

	public Gyms(String gymTrainer, String gymName, String locationName, double latitude, double longtitude,
			double entryFee, double monthlyFee, String gymOwner) {
		super();
		this.gymTrainer = gymTrainer;
		this.gymName = gymName;
		this.locationName = locationName;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.entryFee = entryFee;
		this.monthlyFee = monthlyFee;
		this.gymOwner = gymOwner;
	}

	public Gyms() {
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

	public String getGymOwner() {
		return gymOwner;
	}

	public void setGymOwner(String gymOwner) {
		this.gymOwner = gymOwner;
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

	@Override
	public String toString() {
		return "Gyms [id=" + id + ", gymTrainer=" + gymTrainer + ", gymOwner=" + gymOwner + ", tradeLicenseId="
				+ tradeLicenseId + ", tinNumber=" + tinNumber + ", openingTime=" + openingTime + ", closingTime="
				+ closingTime + ", coverImageId=" + coverImageId + ", gymImages=" + gymImages + ", gymName=" + gymName
				+ ", locationName=" + locationName + ", latitude=" + latitude + ", longtitude=" + longtitude
				+ ", entryFee=" + entryFee + ", monthlyFee=" + monthlyFee + "]";
	}

}

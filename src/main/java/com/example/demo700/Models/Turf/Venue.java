package com.example.demo700.Models.Turf;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "venues")
@Data
public class Venue {

	@Id
	private String id;

	@NonNull
	private String name;
	@NonNull
	private String address;
	private double basePricePerHour;

	private List<String> sportsSupported;
	private List<String> amenities;
	private List<String> photos;

	private Map<String, Double> pricingPolicy;

	
	@NonNull
	private String ownerId;

	public Venue() {

	}

	public Venue(String name, String address, double basePricePerHour, List<String> sportsSupported,
			List<String> amenities, List<String> photos, Map<String, Double> pricingPolicy, String ownerId) {
		super();
		this.name = name;
		this.address = address;
		this.basePricePerHour = basePricePerHour;
		this.sportsSupported = sportsSupported;
		this.amenities = amenities;
		this.photos = photos;
		this.pricingPolicy = pricingPolicy;
		this.ownerId = ownerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getBasePricePerHour() {
		return basePricePerHour;
	}

	public void setBasePricePerHour(double basePricePerHour) {
		this.basePricePerHour = basePricePerHour;
	}

	public List<String> getSportsSupported() {
		return sportsSupported;
	}

	public void setSportsSupported(List<String> sportsSupported) {
		this.sportsSupported = sportsSupported;
	}

	public List<String> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<String> amenities) {
		this.amenities = amenities;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public Map<String, Double> getPricingPolicy() {
		return pricingPolicy;
	}

	public void setPricingPolicy(Map<String, Double> pricingPolicy) {
		this.pricingPolicy = pricingPolicy;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}

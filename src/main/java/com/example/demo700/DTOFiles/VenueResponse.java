package com.example.demo700.DTOFiles;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.Turf.Booking;

public class VenueResponse {

	private String id, discountId;

	private String name;

	private String address;
	private double basePricePerHour;

	private List<String> sportsSupported;

	private List<String> amenities;

	private List<String> photos;

	private Map<String, Double> pricingPolicy;

	private String ownerId, ownerName;

	private String discountCode;

	private Instant expiry;

	private int usageLimit;

	private double percentage;

	private List<Booking> bookings;

	private List<MatchResponse> matches;

	public VenueResponse(String id, String discountId, String name, String address, double basePricePerHour,
			List<String> sportsSupported, List<String> amenities, List<String> photos,
			Map<String, Double> pricingPolicy, String ownerId, String ownerName, String discountCode, Instant expiry,
			int usageLimit, double percentage, List<Booking> bookings, List<MatchResponse> matches) {
		super();
		this.id = id;
		this.discountId = discountId;
		this.name = name;
		this.address = address;
		this.basePricePerHour = basePricePerHour;
		this.sportsSupported = sportsSupported;
		this.amenities = amenities;
		this.photos = photos;
		this.pricingPolicy = pricingPolicy;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.discountCode = discountCode;
		this.expiry = expiry;
		this.usageLimit = usageLimit;
		this.percentage = percentage;
		this.bookings = bookings;
		this.matches = matches;
	}

	public VenueResponse() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDiscountId() {
		return discountId;
	}

	public void setDiscountId(String discountId) {
		this.discountId = discountId;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public Instant getExpiry() {
		return expiry;
	}

	public void setExpiry(Instant expiry) {
		this.expiry = expiry;
	}

	public int getUsageLimit() {
		return usageLimit;
	}

	public void setUsageLimit(int usageLimit) {
		this.usageLimit = usageLimit;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<MatchResponse> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchResponse> matches) {
		this.matches = matches;
	}

	@Override
	public String toString() {
		return "VenueResponse [id=" + id + ", discountId=" + discountId + ", name=" + name + ", address=" + address
				+ ", basePricePerHour=" + basePricePerHour + ", sportsSupported=" + sportsSupported + ", amenities="
				+ amenities + ", photos=" + photos + ", pricingPolicy=" + pricingPolicy + ", ownerId=" + ownerId
				+ ", ownerName=" + ownerName + ", discountCode=" + discountCode + ", expiry=" + expiry + ", usageLimit="
				+ usageLimit + ", percentage=" + percentage + ", bookings=" + bookings + ", matches=" + matches + "]";
	}

}

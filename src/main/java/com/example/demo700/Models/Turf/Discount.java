package com.example.demo700.Models.Turf;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "discounts")
@Data
public class Discount {

	@Id
	private String id;
	private String code;
	private String ownerId;
	private double percentage;
	private Instant expiry;
	private int usageLimit;
	private String venueId;

	public Discount(String code, String ownerId, double percentage, Instant expiry, int usageLimit, String venueId) {
		super();
		this.code = code;
		this.ownerId = ownerId;
		this.percentage = percentage;
		this.expiry = expiry;
		this.usageLimit = usageLimit;
		this.venueId = venueId;
	}

	public Discount() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
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

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

}

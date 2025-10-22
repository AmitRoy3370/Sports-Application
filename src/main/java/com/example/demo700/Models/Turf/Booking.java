package com.example.demo700.Models.Turf;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo700.ENUMS.BookingStatus;

import lombok.Data;

@Document(collection = "bookings")
@Data
public class Booking {

	@Id
	private String id;

	private String venueId;
	private String userId;

	private Instant startTime;
	private Instant endTime;

	private BookingStatus status;
	private double amount;

	private boolean isGroupBooking = false;
	private String groupBookingId;

	public Booking(String venueId, String userId, Instant startTime, Instant endTime, BookingStatus status,
			double amount, boolean isGroupBooking, String groupBookingId) {
		super();
		this.venueId = venueId;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.amount = amount;
		this.isGroupBooking = isGroupBooking;
		this.groupBookingId = groupBookingId;
	}

	public Booking() {
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isGroupBooking() {
		return isGroupBooking;
	}

	public void setGroupBooking(boolean isGroupBooking) {
		this.isGroupBooking = isGroupBooking;
	}

	public String getGroupBookingId() {
		return groupBookingId;
	}

	public void setGroupBookingId(String groupBookingId) {
		this.groupBookingId = groupBookingId;
	}

}

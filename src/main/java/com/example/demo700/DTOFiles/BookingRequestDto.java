package com.example.demo700.DTOFiles;

import java.time.Instant;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Data
public class BookingRequestDto {

	@NonNull
	private String venueId;
	@NonNull
	private String userId;
	@NonNull
	private Instant startTime;
	@NonNull
	private Instant endTime;
	private boolean group;

	public BookingRequestDto(String venueId, String userId, Instant startTime, Instant endTime, boolean isGroup) {
		super();
		this.venueId = venueId;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.group = isGroup;
	}

	public BookingRequestDto() {
		super();
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

	public boolean isGroup() {
		return group;
	}

	public void setGroup(boolean isGroup) {
		this.group = isGroup;
	}

}

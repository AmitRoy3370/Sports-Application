package com.example.demo700.Models.GymModels;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "GymJoinRequest")
public class GymJoinRequest {

	@Id
	private String id;

	@NonNull
	private String userId;

	@NonNull
	private String gymId;

	private Instant requestSendingTime = Instant.now();

	public GymJoinRequest(String userId, String gymId) {
		super();
		this.userId = userId;
		this.gymId = gymId;
	}

	public GymJoinRequest() {
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

	public String getGymId() {
		return gymId;
	}

	public void setGymId(String gymId) {
		this.gymId = gymId;
	}

	public Instant getRequestSendingTime() {
		return requestSendingTime;
	}

	public void setRequestSendingTime(Instant requestSendingTime) {
		this.requestSendingTime = requestSendingTime;
	}

	@Override
	public String toString() {
		return "GymJoinRequest [id=" + id + ", userId=" + userId + ", gymId=" + gymId + ", requestSendingTime="
				+ requestSendingTime + "]";
	}

}

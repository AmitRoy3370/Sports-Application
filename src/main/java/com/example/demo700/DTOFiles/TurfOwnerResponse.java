package com.example.demo700.DTOFiles;

import java.util.List;

public class TurfOwnerResponse {

	private String id;

	private String userId, userName;

	private String name;

	private String phone;

	private List<VenueResponse> venueDetails;

	public TurfOwnerResponse(String id, String userId, String userName, String name, String phone,
			List<VenueResponse> venueDetails) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.name = name;
		this.phone = phone;
		this.venueDetails = venueDetails;
	}

	public TurfOwnerResponse() {
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<VenueResponse> getVenueDetails() {
		return venueDetails;
	}

	public void setVenueDetails(List<VenueResponse> venueDetails) {
		this.venueDetails = venueDetails;
	}

	@Override
	public String toString() {
		return "TurfOwnerResponse [id=" + id + ", userId=" + userId + ", userName=" + userName + ", name=" + name
				+ ", phone=" + phone + ", venueDetails=" + venueDetails + "]";
	}

}

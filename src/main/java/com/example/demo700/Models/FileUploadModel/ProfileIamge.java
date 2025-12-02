package com.example.demo700.Models.FileUploadModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;

@Document(collection = "ProfileImage")
public class ProfileIamge {

	@Id
	String id;

	@NonNull
	String userId;
	@NonNull
	@JsonIgnore
	String imageHex;

	public ProfileIamge(String userId, String imageHex) {
		super();
		this.userId = userId;
		this.imageHex = imageHex;
	}

	public ProfileIamge() {
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

	public String getImageHex() {
		return imageHex;
	}

	public void setImageHex(String imageHex) {
		this.imageHex = imageHex;
	}

	@Override
	public String toString() {
		return "ProfileIamge [id=" + id + ", userId=" + userId + ", imageHex=" + imageHex + "]";
	}

}

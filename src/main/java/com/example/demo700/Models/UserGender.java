package com.example.demo700.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo700.ENUMS.Gender;
import com.mongodb.lang.NonNull;

@Document(collection = "UserGender")
public class UserGender {

	@Id
	private String id;

	@NonNull
	private String userId;

	@NonNull
	private Gender gender;

	public UserGender(String userId, Gender gender) {
		super();
		this.userId = userId;
		this.gender = gender;
	}

	public UserGender() {
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "UserGender [id=" + id + ", userId=" + userId + ", gender=" + gender + "]";
	}

}

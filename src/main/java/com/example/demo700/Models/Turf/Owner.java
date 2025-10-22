package com.example.demo700.Models.Turf;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "Owners")
@Data
public class Owner {

	@Id
	private String id;
	@NonNull
	private String userId;
	@NonNull
	private String name;
	@NonNull
	private String phone;

	public Owner() {

	}

	public Owner(String userId, String name, String phone) {

		this.userId = userId;
		this.name = name;
		this.phone = phone;

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

}

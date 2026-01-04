package com.example.demo700.DTOFiles;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Data
public class LoginRequest {

	@NonNull
	private String email;

	@NonNull
	private String password;

	public LoginRequest(String email, String password) {
		super();
		this.email = email.trim();
		this.password = password.trim();
	}

	public LoginRequest() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

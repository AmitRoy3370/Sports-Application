package com.example.demo700.DTOFiles;

import java.util.Set;

import com.example.demo700.ENUMS.Role;

import lombok.Data;

@Data
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private String userId;
	private Set<Role> roles;

	public JwtResponse(String token, String userId, Set<Role> roles) {
		super();
		this.token = token;
		this.userId = userId;
		this.roles = roles;
	}

	public JwtResponse() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", type=" + type + ", userId=" + userId + ", roles=" + roles + "]";
	}

}

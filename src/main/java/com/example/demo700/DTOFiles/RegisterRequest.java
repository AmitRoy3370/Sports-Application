package com.example.demo700.DTOFiles;

import lombok.Data;

import java.util.Set;

import com.example.demo700.ENUMS.Role;
import com.mongodb.lang.NonNull;

@Data
public class RegisterRequest {

	@NonNull
	private String name;

	@NonNull
	private String email;

	@NonNull
	private String password;

	private Set<Role> roles;

	public RegisterRequest() {
		super();
	}

	public RegisterRequest(String name, String email, String password, String role) {
		super();
		this.name = name.trim();
		this.email = email.trim();
		this.password = password.trim();
		this.roles.add(Role.valueOf(role));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}

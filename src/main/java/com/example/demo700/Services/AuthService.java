package com.example.demo700.Services;

import java.util.List;

import com.example.demo700.DTOFiles.JwtResponse;
import com.example.demo700.DTOFiles.LoginRequest;
import com.example.demo700.DTOFiles.RegisterRequest;
import com.example.demo700.Models.User;

public interface AuthService {
	JwtResponse register(RegisterRequest request);
	
	JwtResponse login(LoginRequest request);
	
	boolean removeUser(String userId, String userTryingToDelete);
	User updateUser(RegisterRequest request, String userId);
	List<User> seeAllUser();
	User searchUserById(String userId);
	User findByName(String name);
	User findByEmail(String email);
	
}
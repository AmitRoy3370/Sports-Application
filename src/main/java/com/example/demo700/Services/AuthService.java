package com.example.demo700.Services;

import com.example.demo700.DTOFiles.JwtResponse;
import com.example.demo700.DTOFiles.LoginRequest;
import com.example.demo700.DTOFiles.RegisterRequest;
import com.example.demo700.Models.User;

public interface AuthService {
	JwtResponse register(RegisterRequest request);
	
	JwtResponse login(LoginRequest request);
	
	boolean removeUser(String userId, String userTryingToDelete);
	User updateUser(RegisterRequest request, String userId);
	
}
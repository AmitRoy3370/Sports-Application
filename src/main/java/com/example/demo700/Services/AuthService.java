package com.example.demo700.Services;

import com.example.demo700.DTOFiles.JwtResponse;
import com.example.demo700.DTOFiles.LoginRequest;
import com.example.demo700.DTOFiles.RegisterRequest;

public interface AuthService {
	JwtResponse register(RegisterRequest request);
	
	JwtResponse login(LoginRequest request);
	
	boolean removeUser(String userId, String userTryingToDelete);
	
}
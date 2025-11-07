package com.example.demo700.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.DTOFiles.JwtResponse;
import com.example.demo700.DTOFiles.LoginRequest;
import com.example.demo700.DTOFiles.RegisterRequest;
import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity<JwtResponse> register(@Validated @RequestBody RegisterRequest request) {
		JwtResponse resp = authService.register(request);
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@Validated @RequestBody LoginRequest request) {
		JwtResponse resp = authService.login(request);
		return ResponseEntity.ok(resp);
	}

}

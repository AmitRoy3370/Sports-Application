package com.example.demo700.Controllers.CheckController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.DTOFiles.JwtResponse;
import com.example.demo700.Security.JwtUtil;

@RestController
@RequestMapping("/api/check")
public class Check {
	
	@Autowired
	JwtUtil jwt;
	
	@PostMapping("/checkJWT")
	public ResponseEntity<?> check(@RequestBody JwtResponse jwtResponse) {
		
		boolean result = jwt.validateToken(jwtResponse.getToken());
		
		return ResponseEntity.status(result ? 200 : 400).body(result);
		
	}

}

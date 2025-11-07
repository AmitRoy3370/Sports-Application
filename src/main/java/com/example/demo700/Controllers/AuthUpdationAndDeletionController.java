package com.example.demo700.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.DTOFiles.RegisterRequest;
import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Services.AuthService;

@RestController
@RequestMapping("/api/user")
public class AuthUpdationAndDeletionController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(@RequestParam String userId, @RequestParam String userTryingToDelete) {

		try {

			System.out.println("I am in the delete rest api...");

			boolean yes = authService.removeUser(userId, userTryingToDelete);

			if (yes) {

				return ResponseEntity.status(200).body("USER deleted successfully...");

			} else {

				return ResponseEntity.status(500).body("User is not deleted...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestParam String userId, @Validated @RequestBody RegisterRequest request) {

		try {

			User user = authService.updateUser(request, userId);

			if (user == null) {

				return ResponseEntity.status(500).body("User is not updated...");

			}

			return ResponseEntity.status(200).body(user);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

}

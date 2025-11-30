package com.example.demo700.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllUser() {

		try {

			List<User> list = authService.seeAllUser();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return ResponseEntity.status(200).body(list);

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	@GetMapping("/searchByUserId")
	public ResponseEntity<?> searchByUserId(@RequestParam String userId) {

		try {

			User user = authService.searchUserById(userId);

			if (user == null) {

				return ResponseEntity.status(404).body("No such user exist at here...");

			}

			return ResponseEntity.status(200).body(user);

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	@GetMapping("/findByName")
	public ResponseEntity<?> searchByName(@RequestParam String name) {
		
		try {
			
			User user = authService.findByName(name);
			
			if(user == null) {
				
				return ResponseEntity.status(500).body("User can't searching...");
				
			}
			
			return ResponseEntity.status(200).body(user);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(404).body(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/findByEmail")
	public ResponseEntity<?> searchByEmail(@RequestParam String email) {
		
		try {
			
			User user = authService.findByEmail(email);
			
			if(user == null) {
				
				return ResponseEntity.status(500).body("User can't searching...");
				
			}
			
			return ResponseEntity.status(200).body(user);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(404).body(e.getMessage());
			
		}
		
	}
	
}

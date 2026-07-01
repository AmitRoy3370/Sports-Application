package com.example.demo700.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.DTOFiles.RegisterRequest;
import com.example.demo700.ENUMS.Role;
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
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or #userTryingToDelete == authentication.principal.id")
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

	@GetMapping("/role/{role}")
	public ResponseEntity<?> findByRole(@PathVariable Role role) {
		
		try {
			
			return ResponseEntity.status(200).body(authService.findByRoles(role));
			
		} catch(Exception e) {
			
			return ResponseEntity.status(404).body(e.getMessage());
			
		}
		
	}
 	
	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllUser(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size) {

		try {
			int safeSize = Math.min(Math.max(size, 1), 200);
			Page<User> users = userRepository.findAll(PageRequest.of(Math.max(page, 0), safeSize));

			if (users.isEmpty()) {
				throw new Exception("No such user exist at here...");
			}

			return ResponseEntity.status(200).body(users.getContent());

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
			
			List<User> user = authService.findByName(name);
			
			if(user == null) {
				
				return ResponseEntity.status(500).body("User can't searching...");
				
			}
			
			return ResponseEntity.status(200).body(user);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(404).body(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/findByNamePrefix")
	public ResponseEntity<?> findByNamePrefix(@RequestParam String namePrefix) {
		
		try {
			
			return ResponseEntity.status(200).body(authService.findByUserNamePrefix(namePrefix));
			
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

package com.example.demo700.Services;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.JwtResponse;
import com.example.demo700.DTOFiles.LoginRequest;
import com.example.demo700.DTOFiles.RegisterRequest;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Security.JwtUtil;
import com.example.demo700.Validator.EmailValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private CyclicCleaner cyclicCleaner;

	private EmailValidator emailValidator;

	@Override
	public JwtResponse register(RegisterRequest request) {
		userRepository.findByEmail(request.getEmail().trim()).ifPresent(u -> {
			throw new RuntimeException("Email already exists");
		});

		emailValidator = new EmailValidator();

		if (!emailValidator.isValidEmail(request.getEmail().trim())) {
			throw new RuntimeException("Email address is not valid...");
		}

		/*try {
			List<User> user = userRepository.findByNameIgnoreCase(request.getName().trim());
			if (user != null || !user.isEmpty()) {
				throw new ArithmeticException();
			}
		} catch (ArithmeticException e) {
			throw new ArithmeticException("User name already exist...");
		} catch (Exception e) {
			// Name doesn't exist - continue
		}*/

		User u = new User();
		u.setName(request.getName().trim());
		u.setEmail(request.getEmail().trim());
		u.setPassword(passwordEncoder.encode(request.getPassword().trim()));

		if (request.getRoles() == null || request.getRoles().isEmpty()) {
			u.setRoles(new HashSet<>());
			u.getRoles().add(Role.ROLE_USER);
		} else {
			u.setRoles(request.getRoles());
		}

		u = userRepository.save(u);

		String token = jwtUtil.generateToken(u);
		return new JwtResponse(token, u.getId());
	}

	@Override
	public JwtResponse login(LoginRequest request) {
		System.out.println("I am at here...");

		User u = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		String token = jwtUtil.generateToken(u);
		return new JwtResponse(token, u.getId());
	}

	@Override
	public boolean removeUser(String userId, String userTryingToDelete) {
		System.out.println("Working for delete...");

		if (userId == null || userTryingToDelete == null) {
			throw new NullPointerException("False request...");
		}

		try {
			User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

			User _user = userRepository.findById(userTryingToDelete)
					.orElseThrow(() -> new Exception("User to delete not found"));

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {
				long count = userRepository.count();
				System.out.println("previous count :- " + count);
				cyclicCleaner.removeUser(userId);
				System.out.println("now :- " + userRepository.count());
				return count != userRepository.count();
			}

			if (!userId.equals(userTryingToDelete)) {
				throw new Exception("Cannot delete another user without admin role");
			}

			long count = userRepository.count();
			cyclicCleaner.removeUser(userId);
			return count != userRepository.count();

		} catch (Exception e) {
			throw new ArithmeticException("The user is not deleted... " + e.getMessage());
		}
	}

	@Override
	public User updateUser(RegisterRequest request, String userId) {
		if (request == null) {
			throw new IllegalArgumentException("Invalid request data");
		}

		String targetEmail = request.getEmail() != null ? request.getEmail().trim() : "";

		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId) );

		if (!existingUser.getEmail().equalsIgnoreCase(targetEmail)) {
			try {
				userRepository.findByEmail(request.getEmail().trim()).ifPresent(u -> {
					throw new RuntimeException("Email already exists");
				});
				
			} catch (NoSuchElementException e) {

			} catch (RuntimeException e) {

				throw new RuntimeException(e.getMessage());

			} catch (Exception e) {

			}
		}

		// Update the existing user object
		existingUser.setName(request.getName().trim());
		existingUser.setEmail(targetEmail);

		if (request.getPassword() != null && !request.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		if (request.getRoles() == null || request.getRoles().isEmpty()) {
			Set<Role> roles = new HashSet<>();
			roles.add(Role.ROLE_USER);
			existingUser.setRoles(roles);
		} else {
			existingUser.setRoles(request.getRoles());
		}

		return userRepository.save(existingUser);
	}

	@Override
	public List<User> seeAllUser() {
		List<User> list = userRepository.findAll();

		if (list.isEmpty()) {
			throw new NoSuchElementException("No such user exist at here...");
		}

		return list;
	}

	@Override
	public List<User> findByRoles(Role role) {
		try {
			List<User> list = userRepository.findByRolesContainingIgnoreCase(role);
			if (list.isEmpty()) {
				throw new Exception();
			}
			return list;
		} catch (Exception e) {
			throw new NoSuchElementException("No such user present of this role");
		}
	}

	@Override
	public User searchUserById(String userId) {
		if (userId == null) {
			throw new NullPointerException("False request...");
		}

		return userRepository.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("No such user exist at here..."));
	}

	@Override
	public List<User> findByName(String name) {
		if (name == null) {
			throw new NullPointerException("False request....");
		}

		List<User> user = userRepository.findByNameIgnoreCase(name.trim());
		if (user == null || user.isEmpty()) {
			throw new NoSuchElementException("No such user find at here...");
		}
		return user;
	}

	@Override
	public User findByEmail(String email) {
		if (email == null) {
			throw new NullPointerException("Email cannot be null");
		}

		return userRepository.findByEmail(email.trim())
				.orElseThrow(() -> new NoSuchElementException("No user found with email: " + email));
	}

	@Override
	public List<User> findByUserNamePrefix(String namePrefix) {
		if (namePrefix == null) {
			throw new NullPointerException("False request...");
		}

		List<User> list = userRepository.findByNameContainingIgnoreCase(namePrefix.trim());
		if (list.isEmpty()) {
			throw new NoSuchElementException("No such user find at here..");
		}
		return list;
	}
}
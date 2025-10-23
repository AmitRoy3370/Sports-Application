package com.example.demo700.Services;

import java.util.HashSet;

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

	@Override
	public JwtResponse register(RegisterRequest request) {

		userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
			throw new RuntimeException("Email already exists");
		});

		User u = new User();
		u.setName(request.getName());
		u.setEmail(request.getEmail());
		u.setPassword(passwordEncoder.encode(request.getPassword()));

		if (request.getRoles() == null || request.getRoles().isEmpty()) {

			u.setRoles(new HashSet<>());
			u.getRoles().add(Role.ROLE_USER);
		} else {
			u.setRoles(request.getRoles());
		}

		u = userRepository.save(u);

		String token = jwtUtil.generateToken(u.getName());
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

		String token = jwtUtil.generateToken(u.getName());
		return new JwtResponse(token, u.getId());
	}

	@Override
	public boolean removeUser(String userId, String userTryingToDelete) {

		if (userId == null || userTryingToDelete == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			User _user = userRepository.findById(userTryingToDelete).get();

			if (_user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = userRepository.count();

				cyclicCleaner.removeUser(userId);

				return count != userRepository.count();

			}

			if (!userId.equals(userTryingToDelete)) {

				throw new Exception();

			}

			long count = userRepository.count();

			cyclicCleaner.removeUser(userId);

			return count != userRepository.count();

		} catch (Exception e) {

			throw new ArithmeticException("The user is not deleted...");

		}

	}

}

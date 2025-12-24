/*package com.example.demo700.Services;

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

		userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
			throw new RuntimeException("Email already exists");
		});

		emailValidator = new EmailValidator();

		if (!emailValidator.isValidEmail(request.getEmail())) {

			throw new RuntimeException("Email adress is not valid...");

		}

		try {

			User user = userRepository.findByNameIgnoreCase(request.getName());

			if (user != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("User name already exist...");

		} catch (Exception e) {

		}

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
		return new JwtResponse(token, u.getId(), u.getRoles());
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
		return new JwtResponse(token, u.getId(), u.getRoles());
	}

	@Override
	public boolean removeUser(String userId, String userTryingToDelete) {

		System.out.println("Working for delete...");

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

				System.out.println("previous count :- " + count);

				cyclicCleaner.removeUser(userId);

				System.out.println("now :- " + userRepository.count());

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

	@Override
	public User updateUser(RegisterRequest request, String userId) {

		if (request == null) {

			throw new ArithmeticException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new RuntimeException("False user update request...");

		}

		try {

			User user = userRepository.findByEmail(request.getEmail()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getId().equals(userId)) {

				throw new RuntimeException("this email is already used by another user...");

			}

		} catch (RuntimeException e) {

			throw new RuntimeException(e.getMessage());

		} catch (Exception e) {

		}

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

		u.setId(userId);

		u = userRepository.save(u);

		return u;
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
	public User searchUserById(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = userRepository.findById(userId).get();

		if (user == null) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		return user;
	}

	@Override
	public User findByName(String name) {

		if (name == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findByNameIgnoreCase(name);

			if (user == null) {

				throw new Exception();

			}

			return user;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	public User findByEmail(String email) {

		if (email == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findByEmail(email).get();

			if (user == null) {

				throw new Exception();

			}

			return user;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find for this " + email + " adress at here...");

		}

	}

}*/

package com.example.demo700.Services;

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

		userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
			throw new RuntimeException("Email already exists");
		});

		emailValidator = new EmailValidator();

		if (!emailValidator.isValidEmail(request.getEmail())) {

			throw new RuntimeException("Email adress is not valid...");

		}

		try {

			User user = userRepository.findByNameIgnoreCase(request.getName());

			if (user != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("User name already exist...");

		} catch (Exception e) {

		}

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

		String token = jwtUtil.generateToken(u);  // ✅ Pass User object to include roles
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

		String token = jwtUtil.generateToken(u);  // ✅ Pass User object to include roles
		return new JwtResponse(token, u.getId());
	}

	@Override
	public boolean removeUser(String userId, String userTryingToDelete) {

		System.out.println("Working for delete...");

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

				System.out.println("previous count :- " + count);

				cyclicCleaner.removeUser(userId);

				System.out.println("now :- " + userRepository.count());

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

	@Override
	public User updateUser(RegisterRequest request, String userId) {

		if (request == null) {

			throw new ArithmeticException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new RuntimeException("False user update request...");

		}

		try {

			User user = userRepository.findByEmail(request.getEmail()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getId().equals(userId)) {

				throw new RuntimeException("this email is already used by another user...");

			}

		} catch (RuntimeException e) {

			throw new RuntimeException(e.getMessage());

		} catch (Exception e) {

		}

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

		u.setId(userId);

		u = userRepository.save(u);

		return u;
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
	public User searchUserById(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = userRepository.findById(userId).get();

		if (user == null) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		return user;
	}

	@Override
	public User findByName(String name) {

		if (name == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findByNameIgnoreCase(name);

			if (user == null) {

				throw new Exception();

			}

			return user;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	public User findByEmail(String email) {

		if (email == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findByEmail(email).get();

			if (user == null) {

				throw new Exception();

			}

			return user;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find for this " + email + " adress at here...");

		}

	}

}


package com.example.demo700.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");

		String email = null;
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			// ✅ Extract email from token
			email = jwtUtil.extractEmail(token);
		}

		// ✅ Use email for authentication
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.validateToken(token)) {

				// ✅ Find user by email (UNIQUE)
				Optional<User> userOpt = userRepository.findByEmail(email);

				if (userOpt.isPresent()) {
					User user = userOpt.get();
					
					List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities =
							user.getRoles().stream()
								.map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.name()))
								.collect(java.util.stream.Collectors.toList());

					UsernamePasswordAuthenticationToken authToken = 
							new UsernamePasswordAuthenticationToken(user, null, authorities);

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}
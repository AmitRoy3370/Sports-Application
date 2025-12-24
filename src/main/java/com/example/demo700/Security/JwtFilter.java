/*package com.example.demo700.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

		String username = null;
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtUtil.extractUsername(token);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.validateToken(token)) {

				User user = userRepository.findByNameIgnoreCase(username);

				if (user != null) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
							new ArrayList<>());

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}

			}
		}

		filterChain.doFilter(request, response);
	}

}*/

/*package com.example.demo700.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

		String username = null;
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtUtil.extractUsername(token);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.validateToken(token)) {

				User user = userRepository.findByNameIgnoreCase(username);

				if (user != null) {
					// FIXED: Convert user roles to Spring Security authorities
					List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = 
							user.getRoles().stream()
								.map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.name()))
								.collect(java.util.stream.Collectors.toList());

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
							authorities);  // ← SET AUTHORITIES FROM USER ROLES

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}

			}
		}

		filterChain.doFilter(request, response);
	}

}*/

/*package com.example.demo700.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

		String username = null;
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtUtil.extractUsername(token);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.validateToken(token)) {

				User user = userRepository.findByNameIgnoreCase(username);

				if (user != null) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
							new ArrayList<>());

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}

			}
		}

		filterChain.doFilter(request, response);
	}

}
*/

package com.example.demo700.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		
		System.out.println("🔍 JwtFilter running for: " + request.getRequestURI());

		String username = null;
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtUtil.extractUsername(token);
			System.out.println("✅ Extracted username: " + username);
		} else {
			System.out.println("⚠️ No Authorization header found");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.validateToken(token)) {

				User user = userRepository.findByNameIgnoreCase(username);

				if (user != null) {
					System.out.println("✅ User found: " + user.getName());
					System.out.println("✅ User roles: " + user.getRoles());
					
					// FIXED: Convert user roles to Spring Security authorities
					List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = 
							user.getRoles().stream()
								.map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.name()))
								.collect(java.util.stream.Collectors.toList());
					
					System.out.println("✅ Authorities set: " + authorities);

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
							authorities);  // ← SET AUTHORITIES FROM USER ROLES

					SecurityContextHolder.getContext().setAuthentication(authToken);
					System.out.println("✅ Authentication set in SecurityContext");
				} else {
					System.out.println("❌ User not found in database");
				}

			} else {
				System.out.println("❌ Token validation failed");
			}
		}

		filterChain.doFilter(request, response);
	}

}


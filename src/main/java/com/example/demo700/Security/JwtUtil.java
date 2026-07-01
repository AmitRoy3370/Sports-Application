package com.example.demo700.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtUtil {

	private final Key key;
	private final long expirationMs;

	public JwtUtil(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration-ms:2592000000}") long expirationMs) {
		if (secret == null || secret.length() < 32) {
			throw new IllegalStateException("jwt.secret must be at least 32 characters (set JWT_SECRET env var)");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	// ✅ Generate token using email (unique identifier)
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email) // ✅ Email as subject
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	// ✅ Generate token from User object
	public String generateToken(User user) {
		List<String> roles = user.getRoles().stream()
				.map(Role::name)
				.collect(Collectors.toList());

		return Jwts.builder()
				.setSubject(user.getEmail()) // ✅ Email as subject
				.claim("userId", user.getId())
				.claim("name", user.getName())
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	// ✅ Extract email from token (subject)
	public String extractEmail(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
		} catch (ExpiredJwtException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	// ✅ Extract username (name) from token claims
	public String extractUsername(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody()
					.get("name", String.class);
		} catch (ExpiredJwtException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	// ✅ Extract userId from token claims
	public String extractUserId(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody()
					.get("userId", String.class);
		} catch (ExpiredJwtException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	// ✅ Validate token
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	// ✅ Validate token with email
	public boolean validateToken(String token, User user) {
		try {
			final String email = extractEmail(token);
			return (email.equals(user.getEmail()) && !isTokenExpired(token));
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isTokenExpired(String token) {
		try {
			Date expiration = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getExpiration();
			return expiration.before(new Date());
		} catch (Exception e) {
			return true;
		}
	}
}
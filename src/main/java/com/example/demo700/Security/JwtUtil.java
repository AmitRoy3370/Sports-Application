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

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String generateToken(User user) {
		List<String> roles = user.getRoles().stream()
				.map(Role::name)
				.collect(Collectors.toList());

		return Jwts.builder()
				.setSubject(user.getName())
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
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

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

}
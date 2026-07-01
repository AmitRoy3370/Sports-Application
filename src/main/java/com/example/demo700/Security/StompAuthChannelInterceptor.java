package com.example.demo700.Security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo700.Models.User;
import com.example.demo700.Repositories.UserRepository;

/**
 * Validates JWT on STOMP CONNECT using email as unique identifier.
 */
@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
			return message;
		}

		// 1. ✅ Validate Authorization header
		String authHeader = accessor.getFirstNativeHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new AccessDeniedException("WebSocket CONNECT requires Authorization: Bearer <token>");
		}

		String token = authHeader.substring(7).trim();
		if (!jwtUtil.validateToken(token)) {
			throw new AccessDeniedException("Invalid or expired WebSocket token");
		}

		// 2. ✅ Extract email from token
		String email = jwtUtil.extractEmail(token);
		if (email == null || email.isBlank()) {
			throw new AccessDeniedException("Token has no email subject");
		}

		// 3. ✅ Find user by email (UNIQUE - no duplicates possible!)
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isEmpty()) {
			throw new AccessDeniedException("User not found for email: " + email);
		}
		
		User user = userOpt.get();

		// 4. ✅ Optional: Verify client headers match (extra security)
		String claimedEmail = accessor.getFirstNativeHeader("email");
		if (claimedEmail != null && !claimedEmail.isBlank()) {
			if (!user.getEmail().equalsIgnoreCase(claimedEmail)) {
				throw new AccessDeniedException("Email header does not match token");
			}
		}

		String claimedUserId = accessor.getFirstNativeHeader("userId");
		if (claimedUserId != null && !claimedUserId.isBlank()) {
			if (!user.getId().equals(claimedUserId)) {
				throw new AccessDeniedException("userId header does not match user found with email");
			}
		}

		// 5. ✅ Build authorities
		List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());

		// 6. ✅ Create authentication token with user object
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(user, null, authorities);

		accessor.setUser(authentication);
		
		// 7. ✅ Store session attributes
		if (accessor.getSessionAttributes() != null) {
			accessor.getSessionAttributes().put("userId", user.getId());
			accessor.getSessionAttributes().put("email", user.getEmail());
			accessor.getSessionAttributes().put("username", user.getName());
			accessor.getSessionAttributes().put("roles", user.getRoles());
		}

		return message;
	}
}
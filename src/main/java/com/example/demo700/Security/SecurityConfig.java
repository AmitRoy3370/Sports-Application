
package com.example.demo700.Security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.MultipartFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	@Value("${cors.allowed-origins:http://localhost:8080,http://localhost:8090,http://localhost:5173,https://sportzil.com,https://www.sportzil.com}")
	private String corsAllowedOrigins;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
						// Public read-only endpoints for marketing site (sportzil.com hero stats/map)
						.requestMatchers(HttpMethod.GET,
								"/api/athelete/seeAll",
								"/api/athelete/count",
								"/api/venues/seeAll",
								"/api/coach/seeAll",
								"/api/athleteLocation/all",
								"/api/venueLocation/seeAll",
								"/api/gyms/all",
								"/api/matches/all",
								"/api/doctor/all",
								"/api/team/seeAll")
						.permitAll()
						// WebSocket handshake — STOMP CONNECT requires JWT (StompAuthChannelInterceptor)
						.requestMatchers("/ws-chat/**", "/ws/**").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		List<String> origins = Arrays.stream(corsAllowedOrigins.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
		configuration.setAllowedOrigins(origins);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));
		configuration.setAllowCredentials(true);
		configuration.setExposedHeaders(Arrays.asList("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public FilterRegistrationBean<MultipartFilter> multipartFilter() {
		FilterRegistrationBean<MultipartFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new MultipartFilter());
		return registrationBean;
	}

}
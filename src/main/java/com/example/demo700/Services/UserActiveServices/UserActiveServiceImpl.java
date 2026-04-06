package com.example.demo700.Services.UserActiveServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.UserActiveResponseDTO;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Model.UserActiveModel.UserActive;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Models.FileUploadModel.ProfileIamge;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;
import com.example.demo700.Repositories.UserActiveRepositories.UserActiveRepository;

import jakarta.annotation.PreDestroy;

@Service
public class UserActiveServiceImpl implements UserActiveService {

	@Autowired
	private UserActiveRepository userActiveRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGenderRepository userGenderRepository;

	@Autowired
	private ProfileImageRepository profileImageRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private AthleteLocationRepository athleteLocationRepository;

	@Autowired
	private AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	private MatchNameRepository matchNameRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public UserActive addUserActive(UserActive userActive) {

		if (userActive == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userActive.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here....");

		}

		try {

			UserActive active = userActiveRepository.findByUserId(userActive.getUserId());

			if (active != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("No such user exist at here....");

		} catch (Exception e) {

		}

		userActive = userActiveRepository.save(userActive);

		if (userActive == null) {

			throw new ArithmeticException("User activeness is not added at here....");

		}

		return userActive;

	}

	@Override
	public UserActive updateUserActive(UserActive userActive, String userId, String id) {

		if (userActive == null || userId == null || id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userActive.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here....");

		}

		try {

			UserActive active = userActiveRepository.findByUserId(userActive.getUserId());

			if (active != null) {

				if (!active.getId().equals(id)) {

					throw new ArithmeticException();

				}

			} else {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("No such user exist at here....");

		} catch (Exception e) {

			throw new NoSuchElementException("Your updating activeness of user not find at here....");

		}

		try {

			UserActive active = userActiveRepository.findById(id).get();

			if (active == null) {

				throw new Exception();

			}

			if (!userActive.getUserId().equals(active.getUserId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user activeness find at here...");

		}

		userActive.setId(id);

		userActive = userActiveRepository.save(userActive);

		if (userActive == null) {

			throw new ArithmeticException("User activeness is not added at here....");

		}

		return userActive;
	}

	@Override
	public UserActiveResponseDTO findById(String id) {

		if (id == null) {

			throw new NullPointerException("False requqest...");

		}

		try {

			UserActive userActive = userActiveRepository.findById(id).get();

			if (userActive == null) {

				throw new Exception();

			}

			return getUserActiveResponse(userActive);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user activeness find at here....");

		}

	}

	@Override
	public List<UserActiveResponseDTO> findAll() {

		try {

			List<UserActive> list = userActiveRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserActiveResponseFromList(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user activeness find at here....");

		}

	}

	@Override
	public UserActiveResponseDTO findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False requqest...");

		}

		try {

			UserActive userActive = userActiveRepository.findByUserId(userId);

			if (userActive == null) {

				throw new Exception();

			}

			return getUserActiveResponse(userActive);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user activeness find at here....");

		}

	}

	@Override
	public List<UserActiveResponseDTO> findByActive(boolean active) {

		try {

			List<UserActive> list = userActiveRepository.findByActive(active);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserActiveResponseFromList(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user activeness find at here....");

		}

	}

	@Override
	public boolean removeUserActive(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = userActiveRepository.count();

				cleaner.removeUserActive(id);

				return count != userActiveRepository.count();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here....");

		}

		try {

			UserActive active = userActiveRepository.findById(id).get();

			if (active == null) {

				throw new Exception();

			}

			if (!userId.equals(active.getUserId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user activeness find at here...");

		}

		long count = userActiveRepository.count();

		cleaner.removeUserActive(id);

		return count != userActiveRepository.count();

	}

	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private UserActiveResponseDTO getUserActiveResponse(UserActive userActive) {

		List<UserActive> list = new ArrayList<>();

		list.add(userActive);

		return getUserActiveResponseFromList(list).get(0);

	}

	private List<UserActiveResponseDTO> getUserActiveResponseFromList(List<UserActive> activeUsers) {

		if (activeUsers == null || activeUsers.isEmpty()) {
			return new ArrayList<>();
		}

		List<UserActiveResponseDTO> responses = new ArrayList<>();

		List<String> allUsersId = activeUsers.stream().filter(Objects::nonNull).map(UserActive::getUserId).distinct()
				.collect(Collectors.toList());

		if (allUsersId.isEmpty()) {
			return responses;
		}

		// ✅ FIX 1: একবারেই সব user fetch করুন
		CompletableFuture<Map<String, User>> userFuture = CompletableFuture.supplyAsync(() -> {
			List<User> users = userRepository.findAllById(allUsersId);
			if (users.isEmpty()) {
				return new HashMap<>();
			}
			return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		}, executor);

		// ✅ FIX 2: Athlete fetch
		List<Athelete> allAthletes = athleteRepository.findByUserIdIn(allUsersId);
		Map<String, Athelete> athleteMap = allAthletes.stream()
				.filter(athlete -> athlete != null && athlete.getUserId() != null).collect(Collectors
						.toMap(Athelete::getUserId, Function.identity(), (existing, replacement) -> existing));

		List<String> allAthleteId = allAthletes.stream().map(Athelete::getId).collect(Collectors.toList());

		// ✅ FIX 3: Location fetch with proper null handling
		CompletableFuture<Map<String, AthleteLocation>> locationFuture = CompletableFuture.supplyAsync(() -> {
			if (allAthleteId.isEmpty()) {
				return new HashMap<>();
			}
			List<AthleteLocation> locations = athleteLocationRepository.findByAthleteIdIn(allAthleteId);
			if (locations.isEmpty()) {
				return new HashMap<>();
			}
			return locations.stream().filter(loc -> loc != null && loc.getAthleteId() != null).collect(Collectors
					.toMap(AthleteLocation::getAthleteId, Function.identity(), (existing, replacement) -> existing));
		}, executor);

		// ✅ FIX 4: Classification fetch
		CompletableFuture<Map<String, AthleteClassification>> classificationFuture = CompletableFuture
				.supplyAsync(() -> {
					if (allAthleteId.isEmpty()) {
						return new HashMap<>();
					}
					List<AthleteClassification> classifications = athleteClassificationRepository
							.findByAthleteIdIn(allAthleteId);
					if (classifications.isEmpty()) {
						return new HashMap<>();
					}
					return classifications.stream().filter(c -> c != null && c.getAthleteId() != null)
							.collect(Collectors.toMap(AthleteClassification::getAthleteId, Function.identity(),
									(existing, replacement) -> existing));
				}, executor);

		// ✅ FIX 5: Gender fetch
		CompletableFuture<Map<String, UserGender>> genderFuture = CompletableFuture.supplyAsync(() -> {
			if (allUsersId.isEmpty()) {
				return new HashMap<>();
			}
			List<UserGender> genders = userGenderRepository.findByUserIdIn(allUsersId);
			if (genders.isEmpty()) {
				return new HashMap<>();
			}
			return genders.stream().filter(g -> g != null && g.getUserId() != null).collect(
					Collectors.toMap(UserGender::getUserId, Function.identity(), (existing, replacement) -> existing));
		}, executor);

		// ✅ FIX 6: Profile image fetch
		CompletableFuture<Map<String, ProfileIamge>> profileImageFuture = CompletableFuture.supplyAsync(() -> {
			if (allUsersId.isEmpty()) {
				return new HashMap<>();
			}
			List<ProfileIamge> images = profileImageRepository.findByUserIdIn(allUsersId);
			if (images == null || images.isEmpty()) {
				return new HashMap<>();
			}
			return images.stream().filter(img -> img != null && img.getUserId() != null).collect(Collectors
					.toMap(ProfileIamge::getUserId, Function.identity(), (existing, replacement) -> existing));
		}, executor);

		// ✅ FIX 7: Match names fetch
		List<String> allEventAttendanceIds = allAthletes.stream()
				.filter(athlete -> athlete != null && athlete.getEventAttendence() != null)
				.flatMap(athlete -> athlete.getEventAttendence().stream()).distinct().collect(Collectors.toList());

		CompletableFuture<Map<String, String>> matchNameFuture = CompletableFuture.supplyAsync(() -> {
			if (allEventAttendanceIds.isEmpty()) {
				return new HashMap<>();
			}
			try {
				List<MatchName> matchNames = matchNameRepository.findByMatchIdIn(allEventAttendanceIds);
				if (matchNames == null || matchNames.isEmpty()) {
					return new HashMap<>();
				}
				return matchNames.stream().filter(mn -> mn != null && mn.getMatchId() != null).collect(Collectors.toMap(
						MatchName::getMatchId,
						mn -> (mn.getName() != null && !mn.getName().isBlank()) ? mn.getName() : "Unknown Match",
						(existing, replacement) -> existing));
			} catch (Exception e) {
				System.err.println("Error fetching match names: " + e.getMessage());
				return new HashMap<>();
			}
		}, executor);

		// Wait for all
		CompletableFuture.allOf(userFuture, locationFuture, classificationFuture, genderFuture, profileImageFuture,
				matchNameFuture).join();

		// Get results
		Map<String, User> userMap = userFuture.join();
		Map<String, AthleteLocation> locationMap = locationFuture.join();
		Map<String, AthleteClassification> classificationMap = classificationFuture.join();
		Map<String, UserGender> genderMap = genderFuture.join();
		Map<String, ProfileIamge> profileImageMap = profileImageFuture.join();
		Map<String, String> matchNameMap = matchNameFuture.join();

		// ✅ FIX 8: Default values for missing match names
		for (String eventId : allEventAttendanceIds) {
			matchNameMap.putIfAbsent(eventId, "Unknown Match");
		}

		// ✅ FIX 9: Build responses with proper null checks
		for (UserActive activeUser : activeUsers) {
			try {
				UserActiveResponseDTO response = new UserActiveResponseDTO();

				// Basic info
				response.setId(activeUser.getId());
				response.setActive(activeUser.isActive());
				response.setUserId(activeUser.getUserId());

				// ✅ User info with null check
				User user = userMap.get(activeUser.getUserId());
				if (user != null) {
					response.setEmail(user.getEmail());
					response.setRoles(user.getRoles());
					response.setUserName(user.getName());
					response.setName(user.getName());
				}

				// ✅ Profile image
				ProfileIamge profileImage = profileImageMap.get(activeUser.getUserId());
				if (profileImage != null && profileImage.getImageHex() != null) {
					response.setImageHex(profileImage.getImageHex());
				}

				// ✅ Gender
				UserGender gender = genderMap.get(activeUser.getUserId());
				if (gender != null) {
					response.setGender(gender.getGender());
					response.setUserGenderId(gender.getId());
				}

				// ✅ Athlete info
				Athelete athlete = athleteMap.get(activeUser.getUserId());
				if (athlete != null) {
					response.setAthleteId(athlete.getId());
					response.setAge(athlete.getAge());
					response.setHeight(athlete.getHeight());
					response.setWeight(athlete.getWeight());
					response.setEventAttendence(athlete.getEventAttendence());
					response.setGameLogs(athlete.getGameLogs());
					response.setPosition(athlete.getPosition());
					response.setHighlightReels(athlete.getHighlightReels());

					// ✅ Match names
					List<String> matchNames = athlete.getEventAttendence() != null
							? athlete.getEventAttendence().stream().filter(Objects::nonNull)
									.map(matchId -> matchNameMap.getOrDefault(matchId, "Unknown Match")).collect(
											Collectors.toList())
							: new ArrayList<>();
					response.setEventNames(matchNames);

					// ✅ Location
					AthleteLocation location = locationMap.get(athlete.getId());
					if (location != null) {
						response.setLattitude(location.getLattitude());
						response.setLongitude(location.getLongitude());
						response.setLocationId(location.getId());
					}

					// ✅ Classification
					AthleteClassification classification = classificationMap.get(athlete.getId());
					if (classification != null) {
						response.setAthleteClassificationId(classification.getId());
						response.setAthleteClassificationTypes(classification.getAthleteClassificationTypes());
					}
				}

				responses.add(response);

			} catch (Exception e) {
				System.err.println(
						"Error building response for user: " + activeUser.getUserId() + " - " + e.getMessage());
			}
		}

		return responses;
	}

	@PreDestroy
	public void cleanup() {
		if (executor != null) {
			executor.shutdown();
			try {
				if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

}

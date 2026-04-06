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

		List<UserActiveResponseDTO> responses = new ArrayList<>();

		List<String> allUsersId = activeUsers.stream().map(UserActive::getUserId).distinct()
				.collect(Collectors.toList());

		CompletableFuture<Map<String, User>> userFuture = CompletableFuture.supplyAsync(() -> userRepository
				.findAllById(allUsersId).stream().collect(Collectors.toMap(User::getId, Function.identity())),
				executor);

		CompletableFuture<Map<String, String>> nameFuture = CompletableFuture
				.supplyAsync(() -> userRepository.findAllById(allUsersId).isEmpty() ? new HashMap<>()
						: userRepository.findAllById(allUsersId).stream()
								.collect(Collectors.toMap(User::getId, User::getName)),
						executor);

		List<Athelete> allAthletes = athleteRepository.findByUserIdIn(allUsersId);

		CompletableFuture<Map<String, Athelete>> athleteFuture = CompletableFuture
				.supplyAsync(
						() -> allAthletes.isEmpty() ? new HashMap<>()
								: allAthletes.stream()
										.collect(Collectors.toMap(Athelete::getUserId, Function.identity(), null)),
						executor);

		List<String> allAthleteId = allAthletes.stream().map(Athelete::getId).collect(Collectors.toList());

		CompletableFuture<Map<String, AthleteLocation>> locationFuture = CompletableFuture.supplyAsync(
				() -> athleteLocationRepository.findByAthleteIdIn(allAthleteId).isEmpty() ? new HashMap<>()
						: athleteLocationRepository.findByAthleteIdIn(allAthleteId).stream()
								.collect(Collectors.toMap(AthleteLocation::getAthleteId, Function.identity())),
				executor);

		CompletableFuture<Map<String, AthleteClassification>> athleteClassificationFuture = CompletableFuture
				.supplyAsync(
						() -> athleteClassificationRepository.findByAthleteIdIn(allAthleteId).isEmpty()
								? new HashMap<>()
								: athleteClassificationRepository.findByAthleteIdIn(allAthleteId).stream().collect(
										Collectors.toMap(AthleteClassification::getAthleteId, Function.identity())),
						executor);

		CompletableFuture<Map<String, UserGender>> genderFuture = CompletableFuture
				.supplyAsync(
						() -> userGenderRepository.findByUserIdIn(allUsersId).isEmpty() ? new HashMap<>()
								: userGenderRepository.findByUserIdIn(allUsersId).stream()
										.collect(Collectors.toMap(UserGender::getUserId, Function.identity(), null)),
						executor);

		CompletableFuture<Map<String, ProfileIamge>> profileImageFuture = CompletableFuture.supplyAsync(() -> {
			try {
				if (allUsersId.isEmpty()) {
					return new HashMap<>();
				}
				List<ProfileIamge> images = profileImageRepository.findByUserIdIn(allUsersId);
				if (images == null || images.isEmpty()) {
					return new HashMap<>();
				}
				return images.stream().filter(img -> img != null && img.getUserId() != null).collect(Collectors
						.toMap(ProfileIamge::getUserId, Function.identity(), (existing, replacement) -> existing // ✅
																													// Proper
																													// merge
																													// function
				));
			} catch (Exception e) {
				System.err.println("Error fetching profile images: " + e.getMessage());
				return new HashMap<>();
			}
		}, executor);

		List<String> allEventAttendanceIds = allAthletes.stream()
				.filter(athlete -> athlete.getEventAttendence() != null) // Avoid
				// null
				.flatMap(athlete -> athlete.getEventAttendence().stream()) // Convert each List to Stream
				.distinct() // Remove duplicates
				.collect(Collectors.toList());

		CompletableFuture<Map<String, String>> matchNameFuture = CompletableFuture.supplyAsync(() -> {
			try {
				List<MatchName> matchNames = matchNameRepository.findByMatchIdIn(allEventAttendanceIds);

				if (matchNames == null || matchNames.isEmpty()) {
					return Collections.emptyMap();
				}

				return matchNames.stream().filter(mn -> mn != null && mn.getMatchId() != null)
						.collect(Collectors.toMap(MatchName::getMatchId,
								mn -> mn.getName() != null && !mn.getName().isBlank() ? mn.getName() : "Unknown Match",
								(existing, replacement) -> existing // Keep first on duplicate
				));

			} catch (Exception e) {
				// log.error("Error fetching match names: {}", e.getMessage());
				return Collections.emptyMap();
			}
		}, executor);

		CompletableFuture.allOf(userFuture, nameFuture, athleteFuture, locationFuture, athleteClassificationFuture,
				genderFuture, profileImageFuture, matchNameFuture).join();

		Map<String, String> userNameMap = nameFuture.join();
		Map<String, Athelete> athletes = athleteFuture.join();
		Map<String, AthleteLocation> athleteLocations = locationFuture.join();
		Map<String, AthleteClassification> classifications = athleteClassificationFuture.join();
		Map<String, UserGender> genders = genderFuture.join();
		Map<String, ProfileIamge> profileImages = profileImageFuture.join();
		Map<String, String> matchNameMap = matchNameFuture.join();
		Map<String, User> userMap = userFuture.join();

		for (UserActive activeUser : activeUsers) {

			UserActiveResponseDTO response = new UserActiveResponseDTO();

			// all user active info

			response.setId(activeUser.getId());
			response.setActive(activeUser.isActive());
			response.setUserId(activeUser.getUserId());

			// all user info

			try {

				response.setUserName(userNameMap.get(activeUser.getUserId()));
				response.setName(response.getUserName());

			} catch (Exception e) {

			}

			response.setRoles(userMap.get(activeUser.getUserId()).getRoles());
			response.setEmail(userMap.get(activeUser.getUserId()).getEmail());

			// collect profile images

			try {

				response.setImageHex(profileImages.get(activeUser.getUserId()).getImageHex());

			} catch (Exception e) {

			}

			// collect user gender

			try {

				response.setGender(genders.get(activeUser.getUserId()).getGender());
				response.setUserGenderId(genders.get(activeUser.getUserId()).getId());

			} catch (Exception e) {

			}

			// all athlete info

			try {

				Athelete athlete = athletes.get(activeUser.getUserId());

				if (athlete == null) {

					throw new Exception();

				}

				response.setAthleteId(athlete.getId());
				response.setAge(athlete.getAge());
				response.setHeight(athlete.getHeight());
				response.setEventAttendence(athlete.getEventAttendence());
				response.setGameLogs(athlete.getGameLogs());
				response.setPosition(athlete.getPosition());
				response.setWeight(athlete.getWeight());
				response.setHighlightReels(athlete.getHighlightReels());

				// collect all the match name

				try {

					List<String> matchNames = athlete.getEventAttendence() != null
							? athlete.getEventAttendence().stream().filter(Objects::nonNull)
									.map(matchId -> matchNameMap.get(matchId)).collect(Collectors.toList())
							: new ArrayList<>();

					response.setEventNames(matchNames);

				} catch (Exception e) {

				}

				// collect all the athlete location

				try {

					AthleteLocation location = athleteLocations.get(athlete.getId());

					response.setLattitude(location.getLattitude());
					response.setLongitude(location.getLongitude());
					response.setLocationId(location.getId());

				} catch (Exception e) {

				}

				// collect the classification

				try {

					AthleteClassification classification = classifications.get(athlete.getId());

					response.setAthleteClassificationId(classification.getId());
					response.setAthleteClassificationTypes(classification.getAthleteClassificationTypes());

				} catch (Exception e) {

				}

			} catch (Exception e) {

			}

			responses.add(response);

		}

		return responses;

	}

}

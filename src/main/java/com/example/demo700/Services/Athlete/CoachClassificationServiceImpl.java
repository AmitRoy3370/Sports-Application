package com.example.demo700.Services.Athlete;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.CoachListResponseDTO;
import com.example.demo700.DTOFiles.CoachResponse;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.CoachClassification;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Models.FileUploadModel.ProfileIamge;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.CoachClassificationRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;
import com.example.demo700.Validator.URLValidator;

@Service
public class CoachClassificationServiceImpl implements CoachClassificationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private CoachClassificationRepository coachClassificationRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	private AthleteLocationRepository athleteLocationRepository;

	@Autowired
	private UserGenderRepository athleteGenderRepository;

	@Autowired
	private MatchNameRepository matchNameRepository;

	@Autowired
	private ProfileImageRepository profileImageRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public CoachClassification addAthleteClassification(CoachClassification coachClassification, String userId) {

		if (coachClassification == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			Coach coach = coachRepository.findByAtheleteId(athlete.getId());

			if (coach == null) {

				throw new Exception();

			}

			if (!coach.getId().equals(coachClassification.getCoachId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("User is not valid to update classification...");

		}

		try {

			CoachClassification classification = coachClassificationRepository
					.findByCoachId(coachClassification.getCoachId());

			if (classification != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("this coaches classification is already setted...");

		} catch (Exception e) {

		}

		coachClassification = coachClassificationRepository.save(coachClassification);

		if (coachClassification == null) {

			throw new ArithmeticException("Coach classification is not added....");

		}

		return coachClassification;

	}

	@Override
	public CoachClassification updateAthleteClassification(CoachClassification coachClassification, String userId,
			String coachClassificationId) {

		if (coachClassification == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			CoachClassification classification = coachClassificationRepository.findById(coachClassificationId).get();

			if (classification == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			Coach coach = coachRepository.findByAtheleteId(athlete.getId());

			if (coach == null) {

				throw new Exception();

			}

			if (!coach.getId().equals(classification.getCoachId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification exist at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			Coach coach = coachRepository.findByAtheleteId(athlete.getId());

			if (coach == null) {

				throw new Exception();

			}

			if (!coach.getId().equals(coachClassification.getCoachId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("User is not valid to update classification...");

		}

		try {

			CoachClassification classification = coachClassificationRepository
					.findByCoachId(coachClassification.getCoachId());

			if (classification != null) {

				if (!classification.getId().equals(coachClassificationId)) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("this coaches classification is already setted...");

		}

		coachClassification.setId(coachClassificationId);

		coachClassification = coachClassificationRepository.save(coachClassification);

		if (coachClassification == null) {

			throw new ArithmeticException("Coach classification is not added....");

		}

		return coachClassification;

	}

	@Override
	public CoachListResponseDTO seeAll(int page, int size, String requestUrl) {

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<CoachClassification> coachPage = coachClassificationRepository.findAll(pageable);

			if (!coachPage.hasContent()) {

				throw new Exception();

			}

			List<CoachClassification> list = coachPage.getContent();

			List<String> allCoachId = list.stream().map(CoachClassification::getCoachId).collect(Collectors.toList());

			List<Coach> allCoaches = coachRepository.findAllById(allCoachId);

			List<CoachResponse> coachResponses = getCoachResponseFromCoachList(allCoaches);

			CoachListResponseDTO response = new CoachListResponseDTO(coachResponses, coachPage.getNumber(),
					coachPage.getSize(), coachPage.getTotalElements(), coachPage.getTotalPages());

			// Build navigation links
			String baseUrl = requestUrl.split("\\?")[0];
			response.setSelfLink(buildUrl(baseUrl, page, size, ""));

			if (coachPage.hasNext()) {
				response.setNextLink(buildUrl(baseUrl, page + 1, size, ""));
			}
			if (coachPage.hasPrevious()) {
				response.setPrevLink(buildUrl(baseUrl, page - 1, size, ""));
			}
			if (coachPage.getTotalPages() > 0) {
				response.setFirstLink(buildUrl(baseUrl, 0, size, ""));
				response.setLastLink(buildUrl(baseUrl, coachPage.getTotalPages() - 1, size, ""));
			}

			response.setCurrentUrl(requestUrl);
			response.setMessage("Found " + coachResponses.size());

			return response;
		} catch (Exception e) {

			throw new NoSuchElementException("No such classification exist at here...");

		}

	}

	@Override
	public CoachResponse findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			CoachClassification classification = coachClassificationRepository.findById(id).get();

			if (classification == null) {

				throw new Exception();

			}

			Coach coach = coachRepository.findById(classification.getCoachId()).get();

			return getCoachResponseFromCoach(coach);

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here...");

		}

	}

	@Override
	public CoachResponse findByCoachId(String coachId) {

		if (coachId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			CoachClassification classification = coachClassificationRepository.findByCoachId(coachId);

			if (classification == null) {

				throw new Exception();

			}

			return getCoachResponseFromCoach(coachRepository.findById(classification.getCoachId()).get());

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification exist at here...");

		}

	}

	@Override
	public CoachListResponseDTO findByAthleteClassificationTypes(AthleteClassificationTypes athleteClassificationTypes,
			int page, int size, String requestUrl) {

		if (athleteClassificationTypes == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<CoachClassification> coachPage = coachClassificationRepository
					.findByCoachClassificationTypes(athleteClassificationTypes, pageable);

			if (!coachPage.hasContent()) {

				throw new Exception();

			}

			List<CoachClassification> list = coachPage.getContent();

			List<String> allCoachId = list.stream().map(CoachClassification::getCoachId).collect(Collectors.toList());

			List<Coach> allCoaches = coachRepository.findAllById(allCoachId);

			List<CoachResponse> coachResponses = getCoachResponseFromCoachList(allCoaches);

			CoachListResponseDTO response = new CoachListResponseDTO(coachResponses, coachPage.getNumber(),
					coachPage.getSize(), coachPage.getTotalElements(), coachPage.getTotalPages());

			// Build navigation links
			String baseUrl = requestUrl.split("\\?")[0];
			response.setSelfLink(
					buildUrl(baseUrl, page, size, "&athleteClassificationTypes=" + athleteClassificationTypes));

			if (coachPage.hasNext()) {
				response.setNextLink(
						buildUrl(baseUrl, page + 1, size, "&athleteClassificationTypes=" + athleteClassificationTypes));
			}
			if (coachPage.hasPrevious()) {
				response.setPrevLink(
						buildUrl(baseUrl, page - 1, size, "&athleteClassificationTypes=" + athleteClassificationTypes));
			}
			if (coachPage.getTotalPages() > 0) {
				response.setFirstLink(
						buildUrl(baseUrl, 0, size, "&athleteClassificationTypes=" + athleteClassificationTypes));
				response.setLastLink(buildUrl(baseUrl, coachPage.getTotalPages() - 1, size, "&athleteClassificationTypes=" + athleteClassificationTypes));
			}

			response.setCurrentUrl(requestUrl);
			response.setMessage("Found " + coachResponses.size());

			return response;

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification exist at here...");

		}
	}

	@Override
	public boolean removeAthleteClassification(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			CoachClassification classification = coachClassificationRepository.findById(id).get();

			if (classification == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = coachClassificationRepository.count();

				cleaner.removeCoachClassification(id);

				return count != coachClassificationRepository.count();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			Coach coach = coachRepository.findByAtheleteId(athlete.getId());

			if (coach == null) {

				throw new Exception();

			}

			if (!coach.getId().equals(classification.getCoachId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification exist at here...");

		}

		long count = coachClassificationRepository.count();

		cleaner.removeCoachClassification(id);

		return count != coachClassificationRepository.count();
	}

	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private CoachResponse getCoachResponseFromCoach(Coach coach) {

		List<Coach> list = new ArrayList<>();

		list.add(coach);

		return getCoachResponseFromCoachList(list).get(0);

	}

	private List<CoachResponse> getCoachResponseFromCoachList(List<Coach> coaches) {

		List<CoachResponse> responses = new ArrayList<>();

		List<String> athleteIds = coaches.stream().filter(Objects::nonNull).map(Coach::getAtheleteId)
				.collect(Collectors.toList());

		List<Athelete> athletes = athleteRepository.findAllById(athleteIds);

		List<String> userIds = athletes.stream().map(Athelete::getUserId).collect(Collectors.toList());

		// 🔥 CORRECT: flatMap on a List of Lists
		List<String> allEventAttendanceIds = athletes.stream().filter(athlete -> athlete.getEventAttendence() != null) // Avoid
																														// null
				.flatMap(athlete -> athlete.getEventAttendence().stream()) // Convert each List to Stream
				.distinct() // Remove duplicates
				.collect(Collectors.toList());

		CompletableFuture<Map<String, Athelete>> athleteFuture = CompletableFuture.supplyAsync(() -> athletes.isEmpty()
				? new HashMap<>()
				: athletes.stream().collect(
						Collectors.toMap(Athelete::getId, Function.identity(), (existing, replacement) -> existing)),
				executor);

		CompletableFuture<Map<String, User>> userFuture = CompletableFuture
				.supplyAsync(() -> userRepository.findAllById(userIds).isEmpty() ? new HashMap<>()
						: userRepository.findAllById(userIds).stream()
								.collect(Collectors.toMap(User::getId, Function.identity())),
						executor);

		CompletableFuture<Map<String, AthleteLocation>> locationFuture = CompletableFuture.supplyAsync(
				() -> athleteLocationRepository.findByAthleteIdIn(athleteIds).isEmpty() ? new HashMap<>()
						: athleteLocationRepository.findByAthleteIdIn(athleteIds).stream()
								.collect(Collectors.toMap(AthleteLocation::getAthleteId, Function.identity())),
				executor);

		CompletableFuture<Map<String, UserGender>> genderFuture = CompletableFuture
				.supplyAsync(
						() -> athleteGenderRepository.findByUserIdIn(userIds).isEmpty() ? new HashMap<>()
								: athleteGenderRepository.findByUserIdIn(userIds).stream()
										.collect(Collectors.toMap(UserGender::getUserId, Function.identity())),
						executor);

		CompletableFuture<Map<String, AthleteClassification>> classificationFuture = CompletableFuture
				.supplyAsync(
						() -> athleteClassificationRepository.findByAthleteIdIn(athleteIds).isEmpty() ? new HashMap<>()
								: athleteClassificationRepository.findByAthleteIdIn(athleteIds).stream().collect(
										Collectors.toMap(AthleteClassification::getAthleteId, Function.identity())),
						executor);

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

		CompletableFuture<Map<String, ProfileIamge>> profileImageFuture = CompletableFuture.supplyAsync(() -> {
			try {
				if (userIds.isEmpty()) {
					return new HashMap<>();
				}
				List<ProfileIamge> images = profileImageRepository.findByUserIdIn(userIds);
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

		CompletableFuture.allOf(userFuture, locationFuture, genderFuture, classificationFuture, matchNameFuture,
				profileImageFuture, athleteFuture).join();

		// Batch fetch with error handling
		Map<String, User> userMap = userFuture.join();
		Map<String, AthleteLocation> locationMap = locationFuture.join();
		Map<String, UserGender> genderMap = genderFuture.join();
		Map<String, AthleteClassification> classificationMap = classificationFuture.join();
		Map<String, String> matchNameMap = matchNameFuture.join();
		Map<String, Athelete> athleteMap = athleteFuture.join();
		Map<String, ProfileIamge> profileImageMap = profileImageFuture.join();

		int index = 0;

		for (String i : allEventAttendanceIds) {

			if (matchNameMap.containsKey(i)) {

			} else {

				String myMatchName = "match-" + (allEventAttendanceIds.size() + index);

				matchNameMap.put(i, myMatchName);

			}

			index++;

		}

		for (Coach coach : coaches) {

			try {

				CoachResponse dto = new CoachResponse();

				Athelete a = athleteMap.get(coach.getAtheleteId());

				dto.setAthleteId(a.getId());
				dto.setAge(a.getAge());
				dto.setHeight(a.getHeight());
				dto.setWeight(a.getWeight());
				dto.setUserId(a.getUserId());
				dto.setPosition(a.getPosition());
				dto.setAthleteTeam(a.getPresentTeam());
				dto.setGameLogs(a.getGameLogs());
				dto.setEventAttendence(a.getEventAttendence());
				dto.setHighlightReels(a.getHighlightReels());

				List<String> matchNames = a.getEventAttendence() != null
						? a.getEventAttendence().stream().filter(Objects::nonNull)
								.map(matchId -> matchNameMap.get(matchId)).collect(Collectors.toList())
						: new ArrayList<>();

				dto.setEventNames(matchNames);

				User user = userMap.get(a.getUserId());
				if (user != null) {
					dto.setName(user.getName());
					dto.setEmail(user.getEmail());
					dto.setRoles(user.getRoles());
				}

				AthleteLocation loc = locationMap.get(a.getId());
				if (loc != null) {
					dto.setLattitude(loc.getLattitude());
					dto.setLongitude(loc.getLongitude());
					dto.setLocationName(loc.getLocationName());
					dto.setLocationId(loc.getId());
				}

				UserGender gender = genderMap.get(a.getUserId());
				if (gender != null) {
					dto.setGender(gender.getGender());
					dto.setUserGenderId(gender.getId());
				}

				AthleteClassification cls = classificationMap.get(a.getId());
				if (cls != null) {
					dto.setAthleteClassificationId(cls.getId());
					dto.setAthleteClassificationTypes(cls.getAthleteClassificationTypes());
				}

				try {

					if (profileImageMap.containsKey(a.getUserId())) {

						dto.setImageHex(profileImageMap.get(a.getUserId()).getImageHex());

					}

				} catch (Exception e) {

				}

				try {

					dto.setId(coach.getId());

					try {

						dto.setAtheletesVideo(coach.getAtheletesVideo());

					} catch (Exception e) {

					}

					try {

						dto.setPresentTeam(coach.getTeamName());

					} catch (Exception e) {

					}

					try {

						dto.setPerformanceTracking(coach.getPerformanceTracking());

					} catch (Exception e) {

					}

					try {

						dto.setTeamName(coach.getTeamName());

					} catch (Exception e) {

					}

				} catch (Exception e) {

				}

				responses.add(dto);

			} catch (Exception e) {

			}

		}

		return responses;

	}

	// ==================== HELPER METHODS ====================

	private String buildUrl(String baseUrl, int page, int size, String additionalParams) {
		return baseUrl + "?page=" + page + "&size=" + size + additionalParams;
	}

}

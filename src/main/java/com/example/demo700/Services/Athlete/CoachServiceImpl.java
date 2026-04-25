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
import org.springframework.data.domain.Sort;
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
import com.example.demo700.Models.Athlete.Team;
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
public class CoachServiceImpl implements CoachService {

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private CoachClassificationRepository coachClassificationRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	private URLValidator urlValidator = new URLValidator();

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
	public Coach addCoach(Coach coach, String userId) {

		if (coach == null || userId == null) {

			throw new NullPointerException("have to give all the input properly...");

		}

		try {

			Athelete athelete = atheleteRepository.findById(coach.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("The given athele is not present....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (!user.getRoles().contains(Role.ROLE_ADMIN) && !user.getRoles().contains(Role.ROLE_COACH)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Your given actioned user is not valid...");

		}

		try {

			Coach list = coachRepository.findByAtheleteId(coach.getAtheleteId());

			if (list != null) {

				return null;

			}

		} catch (Exception e) {

		}

		if (coach.getAtheletesVideo() != null) {

			if (!urlValidator.isValid(coach.getAtheletesVideo())) {

				throw new ArithmeticException("Your given athlete's video url's are not valid...");

			}

		}

		if (coach.getPerformanceTracking() != null) {

			if (!urlValidator.isValid(coach.getPerformanceTracking())) {

				throw new ArithmeticException("Your performance tracking url's are not valid...");

			}

		}

		if (coach.getTeamName() != null) {

			throw new ArithmeticException("No coach can join in any team in time of creation...");

		}

		coach = coachRepository.save(coach);

		if (coach == null) {

			return null;

		}

		return coach;

	}

	@Override
	public List<CoachResponse> seeAll() {

		List<Coach> list = coachRepository.findAll();

		return getCoachResponseFromCoachList(list);
	}

	@Override
	public CoachListResponseDTO seeAllPaginated(int page, int size, String requestUrl) {
		// Validate page and size
		page = Math.max(0, page);
		size = (size > 0 && size <= 100) ? size : 10;

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Coach> coachPage = coachRepository.findAll(pageable);

		List<CoachResponse> coachResponses = getCoachResponseFromCoachList(coachPage.getContent());

		CoachListResponseDTO response = new CoachListResponseDTO(coachResponses, coachPage.getNumber(),
				coachPage.getSize(), coachPage.getTotalElements(), coachPage.getTotalPages());

		// Build navigation links
		String baseUrl = requestUrl.split("\\?")[0];
		String params = "";

		if (requestUrl.contains("?")) {
			String queryString = requestUrl.split("\\?")[1];
			params = "&" + String.join("&", java.util.Arrays.stream(queryString.split("&"))
					.filter(p -> !p.startsWith("page=") && !p.startsWith("size=")).collect(Collectors.toList()));
		}

		response.setSelfLink(buildUrl(baseUrl, page, size, params));

		if (coachPage.hasNext()) {
			response.setNextLink(buildUrl(baseUrl, page + 1, size, params));
		}
		if (coachPage.hasPrevious()) {
			response.setPrevLink(buildUrl(baseUrl, page - 1, size, params));
		}
		if (coachPage.getTotalPages() > 0) {
			response.setFirstLink(buildUrl(baseUrl, 0, size, params));
			response.setLastLink(buildUrl(baseUrl, coachPage.getTotalPages() - 1, size, params));
		}

		response.setCurrentUrl(requestUrl);
		response.setMessage("Successfully retrieved " + coachResponses.size() + " coaches");
		response.setSuggestedPageSize("For better performance, use page size between 5 and 20");

		return response;
	}

	@Override
	public CoachListResponseDTO findByCoachClassificationPaginated(
			AthleteClassificationTypes athleteClassificationTypes, int page, int size, String requestUrl) {

		page = Math.max(0, page);
		size = (size > 0 && size <= 100) ? size : 10;

		if (athleteClassificationTypes == null) {
			throw new NullPointerException("False request....");
		}

		try {
			Pageable pageable = PageRequest.of(page, size);

			// Get athletes with the given classification
			Page<AthleteClassification> classificationPage = athleteClassificationRepository
					.findByAthleteClassificationTypes(athleteClassificationTypes, pageable);

			List<String> athleteIds = classificationPage.getContent().stream().map(AthleteClassification::getAthleteId)
					.collect(Collectors.toList());

			// Find coaches for these athletes
			List<Coach> coaches = coachRepository.findByAtheleteIdIn(athleteIds);

			// Sort coaches to maintain pagination order
			Map<String, Coach> coachMap = coaches.stream()
					.collect(Collectors.toMap(Coach::getAtheleteId, Function.identity()));

			List<Coach> paginatedCoaches = athleteIds.stream().map(coachMap::get).filter(Objects::nonNull)
					.collect(Collectors.toList());

			List<CoachResponse> coachResponses = getCoachResponseFromCoachList(paginatedCoaches);

			CoachListResponseDTO response = new CoachListResponseDTO(coachResponses, classificationPage.getNumber(),
					classificationPage.getSize(), classificationPage.getTotalElements(),
					classificationPage.getTotalPages());

			// Build navigation links
			String baseUrl = requestUrl.split("\\?")[0];
			response.setSelfLink(buildUrl(baseUrl, page, size, ""));

			if (classificationPage.hasNext()) {
				response.setNextLink(buildUrl(baseUrl, page + 1, size, ""));
			}
			if (classificationPage.hasPrevious()) {
				response.setPrevLink(buildUrl(baseUrl, page - 1, size, ""));
			}
			if (classificationPage.getTotalPages() > 0) {
				response.setFirstLink(buildUrl(baseUrl, 0, size, ""));
				response.setLastLink(buildUrl(baseUrl, classificationPage.getTotalPages() - 1, size, ""));
			}

			response.setCurrentUrl(requestUrl);
			response.setMessage(
					"Found " + coachResponses.size() + " coaches with classification: " + athleteClassificationTypes);
			response.setSuggestedPageSize("Use page size 10 for optimal performance");

			return response;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new NoSuchElementException("No such coach find at here....");
		}
	}

	@Override
	public CoachListResponseDTO searchByCoachName(String name, int page, int size, String requestUrl) {
		page = Math.max(0, page);
		size = (size > 0 && size <= 100) ? size : 10;

		if (name == null || name.trim().isEmpty()) {
			return seeAllPaginated(page, size, requestUrl);
		}

		Pageable pageable = PageRequest.of(page, size);

		// First find users with matching names
		List<User> matchingUsers = userRepository.findByNameContainingIgnoreCase(name);
		List<String> userIds = matchingUsers.stream().map(User::getId).collect(Collectors.toList());

		// Find athletes for these users
		List<Athelete> matchingAthletes = atheleteRepository.findByUserIdIn(userIds);
		List<String> athleteIds = matchingAthletes.stream().map(Athelete::getId).collect(Collectors.toList());

		// Find coaches for these athletes
		Page<Coach> coachPage = coachRepository.findByAtheleteIdIn(athleteIds, pageable);

		List<CoachResponse> coachResponses = getCoachResponseFromCoachList(coachPage.getContent());

		CoachListResponseDTO response = new CoachListResponseDTO(coachResponses, coachPage.getNumber(),
				coachPage.getSize(), coachPage.getTotalElements(), coachPage.getTotalPages());

		// Build navigation links
		String baseUrl = requestUrl.split("\\?")[0];
		response.setSelfLink(buildUrl(baseUrl, page, size, "&name=" + name));

		if (coachPage.hasNext()) {
			response.setNextLink(buildUrl(baseUrl, page + 1, size, "&name=" + name));
		}
		if (coachPage.hasPrevious()) {
			response.setPrevLink(buildUrl(baseUrl, page - 1, size, "&name=" + name));
		}
		if (coachPage.getTotalPages() > 0) {
			response.setFirstLink(buildUrl(baseUrl, 0, size, "&name=" + name));
			response.setLastLink(buildUrl(baseUrl, coachPage.getTotalPages() - 1, size, "&name=" + name));
		}

		response.setCurrentUrl(requestUrl);
		response.setMessage("Found " + coachResponses.size() + " coaches matching name: " + name);

		return response;
	}

	@Override
	public CoachListResponseDTO searchByTeamName(String teamName, int page, int size, String requestUrl) {
		page = Math.max(0, page);
		size = (size > 0 && size <= 100) ? size : 10;

		if (teamName == null || teamName.trim().isEmpty()) {
			return seeAllPaginated(page, size, requestUrl);
		}

		Pageable pageable = PageRequest.of(page, size);

		// Find teams with matching names
		List<Team> matchingTeams = teamRepository.findByTeamNameContainingIgnoreCase(teamName);

		// Collect all coach IDs from these teams
		List<String> coachIds = matchingTeams.stream().flatMap(team -> team.getCoaches().stream()).distinct()
				.collect(Collectors.toList());

		// Find coaches by IDs
		Page<Coach> coachPage = coachRepository.findByIdIn(coachIds, pageable);

		List<CoachResponse> coachResponses = getCoachResponseFromCoachList(coachPage.getContent());

		CoachListResponseDTO response = new CoachListResponseDTO(coachResponses, coachPage.getNumber(),
				coachPage.getSize(), coachPage.getTotalElements(), coachPage.getTotalPages());

		// Build navigation links
		String baseUrl = requestUrl.split("\\?")[0];
		response.setSelfLink(buildUrl(baseUrl, page, size, "&teamName=" + teamName));

		if (coachPage.hasNext()) {
			response.setNextLink(buildUrl(baseUrl, page + 1, size, "&teamName=" + teamName));
		}
		if (coachPage.hasPrevious()) {
			response.setPrevLink(buildUrl(baseUrl, page - 1, size, "&teamName=" + teamName));
		}
		if (coachPage.getTotalPages() > 0) {
			response.setFirstLink(buildUrl(baseUrl, 0, size, "&teamName=" + teamName));
			response.setLastLink(buildUrl(baseUrl, coachPage.getTotalPages() - 1, size, "&teamName=" + teamName));
		}

		response.setCurrentUrl(requestUrl);
		response.setMessage("Found " + coachResponses.size() + " coaches in teams matching: " + teamName);

		return response;
	}

	@Override
	public Coach updateCoach(Coach coach, String userId, String coachId) {
		if (coach == null || userId == null) {

			throw new NullPointerException("have to give all the input properly...");

		}

		try {

			Athelete athelete = atheleteRepository.findById(coach.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("The given athele is not present....");

		}

		try {

			User user = userRepository.findById(userId).get();

			System.out.println("User find...");

			System.out.println(user.getId());

			Athelete athelete = atheleteRepository.findByUserId(user.getId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			System.out.println("Athlete find...");

			Coach _coach = coachRepository.findByAtheleteId(athelete.getId());

			if (_coach == null) {

				throw new Exception();

			}

			System.out.println("Coach find by athlete...");

			if (_coach.getId().equals(coachId) && !user.getRoles().contains(Role.ROLE_COACH)) {

				throw new Exception();

			}

			System.out.println("Everything is ok...");

		} catch (Exception e) {

			throw new ArithmeticException("Your given actioned user is not valid...");

		}

		try {

			Coach list = coachRepository.findByAtheleteId(coach.getAtheleteId());

			if (list == null || !(coachRepository.findById(coachId).get() != null
					&& coachRepository.findById(coachId).get().getAtheleteId().equals(list.getAtheleteId()))) {

				return null;

			}

		} catch (Exception e) {

		}

		try {

			Coach _coach = coachRepository.findById(coachId).get();

			if (_coach == null) {

				return null;

			}

		} catch (Exception e) {

			throw new ArithmeticException("No Coach is at here...");

		}

		if (coach.getAtheletesVideo() != null) {

			if (!urlValidator.isValid(coach.getAtheletesVideo())) {

				throw new ArithmeticException("Your given athlete's video url's are not valid...");

			}

		}

		if (coach.getPerformanceTracking() != null) {

			if (!urlValidator.isValid(coach.getPerformanceTracking())) {

				throw new ArithmeticException("Your performance tracking url's are not valid...");

			}

		}

		if (coach.getTeamName() != null) {

			try {

				Team team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

				if (!team.getCoaches().contains(coachId)) {

					throw new Exception();

				}

				if (!team.getTeamName().equalsIgnoreCase(coach.getTeamName())) {

					throw new Exception();

				}

			} catch (Exception e) {

				throw new ArithmeticException("In valid team information...");

			}

		}

		coach.setId(coachId);

		coach = coachRepository.save(coach);

		if (coach == null) {

			return null;

		}

		return coach;
	}

	@Override
	public boolean deleteCoach(String coachId, String userId) {

		try {

			User user = userRepository.findById(userId).get();

			Athelete athelete = atheleteRepository.findByUserId(user.getId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			Coach _coach = coachRepository.findByAtheleteId(athelete.getId());

			if (_coach == null) {

				throw new Exception();

			}

			if (!_coach.getId().equals(coachId) && !user.getRoles().contains(Role.ROLE_ADMIN)) {

				throw new Exception();

			}

			long count = coachRepository.count();

			cleaner.removeCoach(coachId);

			return count != coachRepository.count();

		} catch (Exception e) {

			throw new ArithmeticException("Your given actioned user is not valid...");

		}

	}

	@Override
	public CoachResponse searchCoach(String coachId) {

		if (coachId == null) {

			return null;

		}

		Coach coach = null;

		try {

			coach = coachRepository.findById(coachId).get();

		} catch (Exception e) {

			return null;

		}

		return getCoachResponseFromCoach(coach);

	}

	@Override
	public CoachResponse findByAthleteId(String athleteId) {

		if (athleteId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Coach coach = coachRepository.findByAtheleteId(athleteId);

			if (coach == null) {

				throw new Exception();

			}

			return getCoachResponseFromCoach(coach);

		} catch (Exception e) {

			throw new NoSuchElementException("No such coach find at here...");

		}

	}

	@Override
	public CoachResponse findByCoachId(String coachId) {

		if (coachId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			return getCoachResponseFromCoach(coachRepository.findById(coachId).get());

		} catch (Exception e) {

			throw new NoSuchElementException("No such coach find at here...");

		}

	}

	@Override
	public List<CoachResponse> findByCoachClassification(AthleteClassificationTypes athleteClassificationTypes) {

		if (athleteClassificationTypes == null) {

			throw new NullPointerException("False request....");

		}

		try {

			System.out.println("sending type :- " + athleteClassificationTypes.toString());

			List<Coach> list = new ArrayList<>();

			List<AthleteClassification> athletes = athleteClassificationRepository
					.findByAthleteClassificationTypes(athleteClassificationTypes);

			System.out.println("athletes :- " + athletes.size());

			if (athletes.isEmpty()) {

				System.out.println("can't find any athlete....");

				throw new Exception();

			}

			System.out.println("athletes :- " + athletes.toString());

			List<String> allAthleteId = athletes.stream().map(AthleteClassification::getAthleteId)
					.collect(Collectors.toList());

			list = coachRepository.findByAtheleteIdIn(allAthleteId);

			/*
			 * for (AthleteClassification i : athletes) {
			 * 
			 * try {
			 * 
			 * Athelete athlete = atheleteRepository.findById(i.getAthleteId()).get();
			 * 
			 * System.out.println("getting athlete :- " + athlete.toString());
			 * 
			 * Coach coach = coachRepository.findByAtheleteId(athlete.getId());
			 * 
			 * if (coach != null) {
			 * 
			 * System.out.println("getting coach :- " + coach.toString());
			 * 
			 * list.add(coach);
			 * 
			 * }
			 * 
			 * } catch (Exception e) {
			 * 
			 * }
			 * 
			 * }
			 */

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getCoachResponseFromCoachList(list);

		} catch (Exception e) {

			System.out.println(e.getMessage());

			throw new NoSuchElementException("No such coach find at here....");

		}

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

		List<Athelete> athletes = atheleteRepository.findAllById(athleteIds);

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

		CompletableFuture<Map<String, CoachClassification>> coachClassificationFuture = CompletableFuture
				.supplyAsync(
						() -> coachClassificationRepository.findAll()
								.isEmpty()
										? new HashMap<>()
										: coachClassificationRepository.findAll().stream().filter(Objects::nonNull)
												.filter(coachClassification -> coachClassification.getCoachId() != null)
												.collect(Collectors.toMap(CoachClassification::getCoachId,
														Function.identity(), (existing, replacement) -> existing)),
						executor);

		CompletableFuture.allOf(userFuture, locationFuture, genderFuture, classificationFuture, matchNameFuture,
				profileImageFuture, athleteFuture, coachClassificationFuture).join();

		// Batch fetch with error handling
		Map<String, User> userMap = userFuture.join();
		Map<String, AthleteLocation> locationMap = locationFuture.join();
		Map<String, UserGender> genderMap = genderFuture.join();
		Map<String, AthleteClassification> classificationMap = classificationFuture.join();
		Map<String, String> matchNameMap = matchNameFuture.join();
		Map<String, Athelete> athleteMap = athleteFuture.join();
		Map<String, CoachClassification> coachClassificationMap = coachClassificationFuture.join();
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

				try {

					CoachClassification classification = coachClassificationMap.get(coach.getId());

					dto.setCoachClassificationId(classification.getId());
					dto.setCoachClassificationTypes(classification.getCoachClassificationTypes());

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

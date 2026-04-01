package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.ENUMS.Gender;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Models.FileUploadModel.ProfileIamge;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;
import com.example.demo700.Validator.URLValidator;

@Service
public class AtheleteServiceImpl implements AtheleteService {

	@Autowired
	AtheleteRepository athleteRepository;

	@Autowired
	AthleteLocationRepository athleteLocationRepository;

	@Autowired
	AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	UserGenderRepository athleteGenderRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	MatchNameRepository matchNameRepository;

	@Autowired
	ProfileImageRepository profileImageRepository;

	URLValidator urlValidator = new URLValidator();

	@Autowired
	CyclicCleaner cleaner;

	// Optimized pagination settings for 50TB database
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 50; // Reduced from 100 to prevent memory issues
	private static final int MAX_BATCH_SIZE = 200; // Reduced from 500 for better memory management

	@Value("${athlete.query.timeout:30000}")
	private long queryTimeout;

	@Override
	public Athelete addAthelete(Athelete athelete, String userId) {

		if (athelete == null || userId == null) {
			throw new NullPointerException("Have to take input of all data...");
		}

		User _user = userRepository.findById(userId)
				.orElseThrow(() -> new ArithmeticException("False user's request..."));

		if (!_user.getRoles().contains(Role.ROLE_ADMIN) && !_user.getRoles().contains(Role.ROLE_ATHLETE)) {
			throw new ArithmeticException("Only admin can add a player...");
		}

		System.out.println(athelete.toString());

		User user = userRepository.findById(athelete.getUserId())
				.orElseThrow(() -> new ArithmeticException("Wrong user id..."));

		if (!user.getRoles().contains(Role.ROLE_ATHLETE)) {
			throw new ArithmeticException("False user request...");
		}

		Optional<Athelete> existingAthlete = athleteRepository.findByUserId(athelete.getUserId());
		if (existingAthlete.isPresent()) {
			return null;
		}

		System.out.println("I am before the athelete saving...");

		if (athelete.getHighlightReels() != null) {
			if (!urlValidator.isValid(athelete.getHighlightReels())) {
				throw new ArithmeticException("Your match rell url's are not valid...");
			}
		}

		if (athelete.getGameLogs() != null) {
			if (!urlValidator.isValid(athelete.getGameLogs())) {
				throw new ArithmeticException("Your game logs url are not valid....");
			}
		}

		if (athelete.getPresentTeam() != null) {
			throw new ArithmeticException("No athlete can join a team in time of creation...");
		}

		if (athelete.getEventAttendence() != null && !athelete.getEventAttendence().isEmpty()) {
			throw new ArithmeticException("Match information is not valid...");
		}

		if (athelete.getAge() < 5) {
			throw new ArithmeticException("Age less than 5 years can't be an athlete...");
		}

		if (athelete.getHeight() < 3.0) {
			throw new ArithmeticException("Any atheltes height can't be less than 3");
		}

		athelete = athleteRepository.save(athelete);

		if (athelete == null) {
			return null;
		}

		return athelete;
	}

	// 🔥 OPTIMIZED PAGINATED VERSION for 50TB database
	@Override
	public AthleteListResponseDTO seeAll(int page, int size) {
		// Validate and optimize pagination parameters
		page = Math.max(0, page);
		size = validatePageSize(size);

		// Use indexed field for sorting to improve performance
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "_id"));
		Page<Athelete> athletePage = athleteRepository.findAll(pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, athletePage.getTotalElements(),
					athletePage.getTotalPages());
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	// 🔥 DEPRECATED - Will cause timeout for 50TB database
	@Override
	@Deprecated
	public List<AthleteRequestDTO> seeAll() {
		throw new UnsupportedOperationException(
				"This method is disabled for 50TB database. Use seeAll(page, size) with pagination instead.");
	}

	@Override
	public Athelete updateAthelete(Athelete athelete, String userId, String atheleteId) {

		if (athelete == null || userId == null || atheleteId == null) {
			throw new NullPointerException("Have to take input of all data...");
		}

		Athelete _athelete = athleteRepository.findById(atheleteId)
				.orElseThrow(() -> new ArithmeticException("No athelete present to update..."));

		User _user = userRepository.findById(userId)
				.orElseThrow(() -> new ArithmeticException("False user's request..."));

		if (!_user.getRoles().contains(Role.ROLE_ATHLETE) || !_user.getId().equals(_athelete.getUserId())) {
			throw new ArithmeticException("Only athelete can update ownself...");
		}

		System.out.println(athelete.toString());

		User user = userRepository.findById(athelete.getUserId())
				.orElseThrow(() -> new ArithmeticException("Wrong user id..."));

		Athelete __athlete = athleteRepository.findByUserId(user.getId())
				.orElseThrow(() -> new ArithmeticException("Athlete not found"));

		if (!user.getRoles().contains(Role.ROLE_ATHLETE) || !__athlete.getId().equals(atheleteId)) {
			throw new ArithmeticException("False user request...");
		}

		try {
			if (__athlete.getPresentTeam() != null) {
				Team team1 = teamRepository.findByTeamNameIgnoreCase(__athlete.getPresentTeam());

				if (team1 == null) {
					throw new Exception();
				}

				if (!team1.getAtheletes().contains(atheleteId)) {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			throw new ArithmeticException("Team information is not valid...");
		}

		System.out.println("I am before the athelete saving...");

		if (athelete.getHighlightReels() != null) {
			if (!urlValidator.isValid(athelete.getHighlightReels())) {
				throw new ArithmeticException("Your match rell url's are not valid...");
			}
		}

		if (athelete.getGameLogs() != null) {
			if (!urlValidator.isValid(athelete.getGameLogs())) {
				throw new ArithmeticException("Your game logs url are not valid....");
			}
		}

		try {
			if (athelete.getEventAttendence() != null && !athelete.getEventAttendence().isEmpty()) {
				for (String i : athelete.getEventAttendence()) {
					Match match = matchRepository.findById(i).orElseThrow(() -> new Exception());

					List<Team> teams = teamRepository.findByMatchesContainingIgnoreCase(match.getId());

					if (teams.isEmpty()) {
						throw new Exception();
					}

					boolean find = false;
					for (Team team : teams) {
						if (team.getAtheletes().contains(_athelete.getId())) {
							find = true;
							break;
						}
					}

					if (!find) {
						throw new Exception();
					}
				}
			}
		} catch (Exception e) {
			throw new ArithmeticException("Match information is not valid...");
		}

		if (athelete.getAge() < 5) {
			throw new ArithmeticException("Age less than 5 years can't be an athlete...");
		}

		if (athelete.getHeight() < 3.0) {
			throw new ArithmeticException("Any atheltes height can't be less than 3");
		}

		athelete.setId(_athelete.getId());
		athelete = athleteRepository.save(athelete);

		if (athelete == null) {
			return null;
		}

		return athelete;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean deleteAthelete(String atheleteId, String userId) {

		if (atheleteId == null || userId == null) {
			throw new NullPointerException("false request...");
		}

		Athelete athelete = athleteRepository.findById(atheleteId)
				.orElseThrow(() -> new ArithmeticException("No such athelete present at here..."));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ArithmeticException("No such user present at here..."));

		if (user.getRoles().contains(Role.ROLE_ADMIN)
				|| athleteRepository.findByUserId(userId).map(Athelete::getId).orElse("").equals(atheleteId)) {

			long count = athleteRepository.count();
			cleaner.removeAthelete(atheleteId);
			boolean yes = count != athleteRepository.count();

			if (yes) {
				try {
					Team team = teamRepository.findByAtheletesContainingIgnoreCase(atheleteId);
					if (team != null) {
						team.getAtheletes().remove(atheleteId);
						teamRepository.save(team);
					}
				} catch (Exception e) {
					// Log error but continue
				}
			}
			return yes;
		} else {
			return false;
		}
	}

	// 🔥 OPTIMIZED SEARCH METHODS WITH PAGINATION
	@Override
	public AthleteListResponseDTO searchAtheleteByTeamName(String teamName, int page, int size) {
		if (teamName == null) {
			throw new NullPointerException("No athelete find at here...");
		}

		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "presentTeam"));
		Page<Athelete> athletePage = athleteRepository.findByPresentTeamIgnoreCase(teamName, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByAgeLessThan(int age, int page, int size) {
		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "age"));
		Page<Athelete> athletePage = athleteRepository.findByAgeLessThan(age, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByHeightGreaterThan(double height, int page, int size) {
		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "height"));
		Page<Athelete> athletePage = athleteRepository.findByHeightGreaterThan(height, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByWeightLessThan(double weight, int page, int size) {
		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "weight"));
		Page<Athelete> athletePage = athleteRepository.findByWeightLessThan(weight, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	// 🔥 NON-PAGINATED VERSIONS WITH WARNINGS FOR 50TB DATABASE
	@Override
	public List<AthleteRequestDTO> searchAtheleteByTeamName(String teamName) {
		if (teamName == null) {
			throw new NullPointerException("False request....");
		}

		System.err.println("⚠️ WARNING: Using non-paginated search for team: " + teamName
				+ ". This will be SLOW for 50TB database. Use searchAtheleteByTeamName(teamName, page, size) instead.");

		List<Athelete> athletes = athleteRepository.findByPresentTeamIgnoreCase(teamName);

		if (athletes.isEmpty()) {
			throw new NoSuchElementException("No athletes found for team: " + teamName);
		}

		// Limit results to prevent memory issues
		if (athletes.size() > 1000) {
			System.err.println(
					"⚠️ WARNING: Large result set (" + athletes.size() + ") detected. Consider using pagination.");
			athletes = athletes.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(athletes);
	}

	@Override
	public List<AthleteRequestDTO> findByAgeLessThan(int age) {
		System.err.println(
				"⚠️ WARNING: Using non-paginated age search. This will be SLOW for 50TB database. Use findByAgeLessThan(age, page, size) instead.");

		List<Athelete> athletes = athleteRepository.findByAgeLessThan(age);

		if (athletes.isEmpty()) {
			throw new NoSuchElementException("No athletes found with age less than: " + age);
		}

		if (athletes.size() > 1000) {
			athletes = athletes.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(athletes);
	}

	@Override
	public List<AthleteRequestDTO> findByHeightGreaterThan(double height) {
		System.err.println(
				"⚠️ WARNING: Using non-paginated height search. This will be SLOW for 50TB database. Use findByHeightGreaterThan(height, page, size) instead.");

		List<Athelete> athletes = athleteRepository.findByHeightGreaterThan(height);

		if (athletes.isEmpty()) {
			throw new NoSuchElementException("No athletes found with height greater than: " + height);
		}

		if (athletes.size() > 1000) {
			athletes = athletes.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(athletes);
	}

	@Override
	public List<AthleteRequestDTO> findByWeightLessThan(double weight) {
		System.err.println(
				"⚠️ WARNING: Using non-paginated weight search. This will be SLOW for 50TB database. Use findByWeightLessThan(weight, page, size) instead.");

		List<Athelete> athletes = athleteRepository.findByWeightLessThan(weight);

		if (athletes.isEmpty()) {
			throw new NoSuchElementException("No athletes found with weight less than: " + weight);
		}

		if (athletes.size() > 1000) {
			athletes = athletes.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(athletes);
	}

	@Override
	public List<AthleteRequestDTO> findByPresentTeamIgnoreCase(String teamName) {
		if (teamName == null) {
			throw new NullPointerException("Invalid request...");
		}

		System.err.println("⚠️ WARNING: Using non-paginated team search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.findByPresentTeamIgnoreCase(teamName);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public List<AthleteRequestDTO> findByPosition(String position) {
		System.err.println("⚠️ WARNING: Using non-paginated position search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.findByPositionContainingIgnoreCase(position);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public List<AthleteRequestDTO> findByEventAttendenceContainingIgnoreCase(String eventName) {
		if (eventName == null) {
			throw new NullPointerException("Invalid request...");
		}

		System.err.println("⚠️ WARNING: Using non-paginated event search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.findByEventAttendenceContainingIgnoreCase(eventName);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public List<AthleteRequestDTO> findByGameLogsContainingIgnoreCase(String gameLog) {
		if (gameLog == null) {
			throw new NullPointerException("Invalid request...");
		}

		System.err.println("⚠️ WARNING: Using non-paginated game log search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.findByGameLogsContainingIgnoreCase(gameLog);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public List<AthleteRequestDTO> findByAgeLessThanAndHeightGreaterThan(int age, double height) {
		System.err.println("⚠️ WARNING: Using non-paginated combined search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.findByAgeLessThanAndHeightGreaterThan(age, height);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public List<AthleteRequestDTO> searchByTeamNamePartial(String partialName) {
		if (partialName == null) {
			throw new NullPointerException("Invalid request...");
		}

		System.err.println("⚠️ WARNING: Using non-paginated partial team search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.searchByTeamNamePartial(partialName);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public List<AthleteRequestDTO> findByMultipleEvents(List<String> eventNames) {
		if (eventNames == null) {
			throw new NullPointerException("Invalid request...");
		}

		System.err.println(
				"⚠️ WARNING: Using non-paginated multiple events search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.findByMultipleEvents(eventNames);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public List<AthleteRequestDTO> findByWeightRange(double min, double max) {
		System.err.println("⚠️ WARNING: Using non-paginated weight range search. This will be SLOW for 50TB database.");

		List<Athelete> list = athleteRepository.findByWeightRange(min, max);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		if (list.size() > 1000) {
			list = list.subList(0, 1000);
		}

		return getListDetailsFromAthleteList(list);
	}

	@Override
	public Optional<AthleteRequestDTO> findByUserId(String userId) {
		if (userId == null) {
			throw new NullPointerException("Invalid request...");
		}

		Optional<Athelete> athleteOpt = athleteRepository.findByUserId(userId);
		if (athleteOpt.isEmpty()) {
			throw new NoSuchElementException("No athlete found...");
		}

		AthleteRequestDTO dto = getDetailsFromAthleteId(athleteOpt.get().getId());
		return Optional.of(dto);
	}

	public AthleteRequestDTO searchByAthleteId(String athleteId) {
		if (athleteId == null) {
			throw new NullPointerException("False request...");
		}
		try {
			return getDetailsFromAthleteId(athleteId);
		} catch (Exception e) {
			throw new NoSuchElementException("No such athlete fin at here...");
		}
	}

	@Override
	public boolean deleteByUserId(String userId, String actionUserId) {
		if (userId == null || actionUserId == null) {
			throw new NullPointerException("false request...");
		}

		User user = userRepository.findById(actionUserId)
				.orElseThrow(() -> new ArithmeticException("unsafe operation..."));

		Athelete athelete = athleteRepository.findById(userId).orElse(null);

		if (athelete == null) {
			return false;
		}

		if (!user.getRoles().contains(Role.ROLE_ADMIN) && !athelete.getUserId().equals(actionUserId)) {
			throw new ArithmeticException("Only center admin can remove atheletes...");
		}

		long count = athleteRepository.count();
		cleaner.removeAthelete(athelete.getId());
		boolean yes = count != athleteRepository.count();
		return yes;
	}

	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	// 🔥 OPTIMIZED BATCH FETCHING for 50TB database
	private List<AthleteRequestDTO> getListDetailsFromAthleteList(List<Athelete> athletes) {
		if (athletes == null || athletes.isEmpty()) {
			return new ArrayList<>();
		}

		// Stricter batch size limit for 50TB database
		if (athletes.size() > MAX_BATCH_SIZE) {
			throw new IllegalArgumentException("Batch size too large. Max " + MAX_BATCH_SIZE
					+ " athletes per batch. Current size: " + athletes.size());
		}

		// Extract IDs
		List<String> athleteIds = athletes.stream().map(Athelete::getId).collect(Collectors.toList());

		List<String> userIds = athletes.stream().map(Athelete::getUserId).collect(Collectors.toList());

		// 🔥 CORRECT: flatMap on a List of Lists
		List<String> allEventAttendanceIds = athletes.stream().filter(athlete -> athlete.getEventAttendence() != null) // Avoid
																														// null
				.flatMap(athlete -> athlete.getEventAttendence().stream()) // Convert each List to Stream
				.distinct() // Remove duplicates
				.collect(Collectors.toList());

		CompletableFuture<Map<String, User>> userFuture = CompletableFuture.supplyAsync(() -> userRepository
				.findAllById(userIds).stream().collect(Collectors.toMap(User::getId, Function.identity())), executor);

		CompletableFuture<Map<String, AthleteLocation>> locationFuture = CompletableFuture
				.supplyAsync(
						() -> athleteLocationRepository.findByAthleteIdIn(athleteIds).stream()
								.collect(Collectors.toMap(AthleteLocation::getAthleteId, Function.identity())),
						executor);

		CompletableFuture<Map<String, UserGender>> genderFuture = CompletableFuture
				.supplyAsync(() -> athleteGenderRepository.findByUserIdIn(userIds).stream()
						.collect(Collectors.toMap(UserGender::getUserId, Function.identity())), executor);

		CompletableFuture<Map<String, AthleteClassification>> classificationFuture = CompletableFuture
				.supplyAsync(
						() -> athleteClassificationRepository.findByAthleteIdIn(athleteIds).stream()
								.collect(Collectors.toMap(AthleteClassification::getAthleteId, Function.identity())),
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

		CompletableFuture<Map<String, ProfileIamge>> profileImageFuture = CompletableFuture
		        .supplyAsync(() -> {
		            try {
		                if (userIds.isEmpty()) {
		                    return new HashMap<>();
		                }
		                List<ProfileIamge> images = profileImageRepository.findByUserIdIn(userIds);
		                if (images == null || images.isEmpty()) {
		                    return new HashMap<>();
		                }
		                return images.stream()
		                    .filter(img -> img != null && img.getUserId() != null)
		                    .collect(Collectors.toMap(
		                        ProfileIamge::getUserId,
		                        Function.identity(),
		                        (existing, replacement) -> existing  // ✅ Proper merge function
		                    ));
		            } catch (Exception e) {
		                System.err.println("Error fetching profile images: " + e.getMessage());
		                return new HashMap<>();
		            }
		        }, executor);


		CompletableFuture.allOf(userFuture, locationFuture, genderFuture, classificationFuture, matchNameFuture,
				profileImageFuture).join();

		// Batch fetch with error handling
		Map<String, User> userMap = userFuture.join();
		Map<String, AthleteLocation> locationMap = locationFuture.join();
		Map<String, UserGender> genderMap = genderFuture.join();
		Map<String, AthleteClassification> classificationMap = classificationFuture.join();
		Map<String, String> matchNameMap = matchNameFuture.join();
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

		// Build DTOs
		List<AthleteRequestDTO> dtoList = new ArrayList<>(athletes.size());

		for (Athelete a : athletes) {
			try {
				AthleteRequestDTO dto = new AthleteRequestDTO();

				dto.setId(a.getId());
				dto.setAge(a.getAge());
				dto.setHeight(a.getHeight());
				dto.setWeight(a.getWeight());
				dto.setUserId(a.getUserId());
				dto.setPosition(a.getPosition());
				dto.setPresentTeam(a.getPresentTeam());
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
					dto.setAthleteClassificationTypes(cls.getAthleteClassificationTypes());
				}

				try {

					if (profileImageMap.containsKey(a.getUserId())) {

						dto.setImageHex(profileImageMap.get(a.getUserId()).getImageHex());

					}

				} catch (Exception e) {

				}

				dtoList.add(dto);
			} catch (Exception e) {
				System.err.println("Error building DTO for athlete: " + a.getId());
			}
		}

		return dtoList;
	}

	private AthleteRequestDTO getDetailsFromAthleteId(String athleteId) {
		Athelete athleteDetails = athleteRepository.findById(athleteId)
				.orElseThrow(() -> new NoSuchElementException("Athlete not found"));

		List<Athelete> athlete = new ArrayList<>();

		athlete.add(athleteDetails);

		return getListDetailsFromAthleteList(athlete).get(0);
	}

	// 🔥 Helper method to validate page size for 50TB database
	private int validatePageSize(int size) {
		if (size < 1) {
			return DEFAULT_PAGE_SIZE;
		}
		if (size > MAX_PAGE_SIZE) {
			System.err.println("⚠️ Requested page size " + size + " exceeds maximum " + MAX_PAGE_SIZE + ". Reducing to "
					+ MAX_PAGE_SIZE);
			return MAX_PAGE_SIZE;
		}
		return size;
	}

	// Add these methods to your AtheleteServiceImpl class

	@Override
	public AthleteListResponseDTO findByPresentTeamIgnoreCase(String teamName, int page, int size) {
		if (teamName == null) {
			throw new NullPointerException("Invalid request...");
		}

		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "presentTeam"));
		Page<Athelete> athletePage = athleteRepository.findByPresentTeamIgnoreCase(teamName, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByPosition(String position, int page, int size) {
		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "position"));
		Page<Athelete> athletePage = athleteRepository.findByPositionContainingIgnoreCase(position, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByEventAttendenceContainingIgnoreCase(String eventName, int page, int size) {
		if (eventName == null) {
			throw new NullPointerException("Invalid request...");
		}

		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size);
		Page<Athelete> athletePage = athleteRepository.findByEventAttendenceContainingIgnoreCase(eventName, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByGameLogsContainingIgnoreCase(String gameLog, int page, int size) {
		if (gameLog == null) {
			throw new NullPointerException("Invalid request...");
		}

		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size);
		Page<Athelete> athletePage = athleteRepository.findByGameLogsContainingIgnoreCase(gameLog, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByAgeLessThanAndHeightGreaterThan(int age, double height, int page, int size) {
		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size);
		Page<Athelete> athletePage = athleteRepository.findByAgeLessThanAndHeightGreaterThan(age, height, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO searchByTeamNamePartial(String partialName, int page, int size) {
		if (partialName == null) {
			throw new NullPointerException("Invalid request...");
		}

		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size);
		Page<Athelete> athletePage = athleteRepository.searchByTeamNamePartial(partialName, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByMultipleEvents(List<String> eventNames, int page, int size) {
		if (eventNames == null || eventNames.isEmpty()) {
			throw new NullPointerException("Invalid request...");
		}

		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size);
		Page<Athelete> athletePage = athleteRepository.findByMultipleEvents(eventNames, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	@Override
	public AthleteListResponseDTO findByWeightRange(double min, double max, int page, int size) {
		page = Math.max(0, page);
		size = validatePageSize(size);

		Pageable pageable = PageRequest.of(page, size);
		Page<Athelete> athletePage = athleteRepository.findByWeightRange(min, max, pageable);

		if (!athletePage.hasContent()) {
			return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
		}

		List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());

		return new AthleteListResponseDTO(athletes, page, size, athletePage.getTotalElements(),
				athletePage.getTotalPages());
	}

	// 🔥 Helper method to get total count
	public long getTotalAthleteCount() {
		return athleteRepository.count();
	}

	@Override
	public AthleteListResponseDTO findByGender(String gender, int page, int size) {

		try {

			Gender _gender = Gender.valueOf(gender);

			if (_gender == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such gender present at here...");

		}

		try {

			List<UserGender> users = athleteGenderRepository.findByGender(Gender.valueOf(gender));

			if (users.isEmpty()) {

				throw new Exception();

			}

			List<String> userIds = users.stream().map(UserGender::getUserId).distinct().collect(Collectors.toList());

			Pageable pageable = PageRequest.of(page, size);

			Page<Athelete> athletes = athleteRepository.findByUserIdIn(userIds, pageable);

			if (!athletes.hasContent()) {

				throw new Exception();

			}

			List<AthleteRequestDTO> list = getListDetailsFromAthleteList(athletes.toList());

			if (list.isEmpty()) {

				throw new Exception();

			}

			return new AthleteListResponseDTO(list, page, size, athletes.getTotalElements(), athletes.getTotalPages());

		} catch (Exception e) {

			throw new NoSuchElementException("No such athlete find at here...");

		}

	}

	@Override
	public List<AthleteRequestDTO> findByNamePartial(String partialName) {

		if (partialName == null) {

			throw new NullPointerException("False request...");

		}

		List<User> users = userRepository.findByNameContainingIgnoreCase(partialName);

		if (users.isEmpty()) {

			throw new NoSuchElementException("No such user find at here...");

		}

		List<String> usersId = users.stream().map(User::getId).distinct().collect(Collectors.toList());

		List<Athelete> athletes = athleteRepository.findByUserIdIn(usersId);

		if (athletes.isEmpty()) {

			throw new NoSuchElementException("No such athlete present with this name");

		}

		return getListDetailsFromAthleteList(athletes);

	}
}
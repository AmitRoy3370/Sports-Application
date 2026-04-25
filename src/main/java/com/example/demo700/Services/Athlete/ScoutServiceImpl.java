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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.ScoutResponse;
import com.example.demo700.DTOFiles.ScoutsListResponseDTO;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Models.Athlete.ScoutClassification;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Models.FileUploadModel.ProfileIamge;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.ScoutClassificationRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;

@Service
public class ScoutServiceImpl implements ScoutService {

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Autowired
	private ScoutClassificationRepository scoutClassificationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	AthleteLocationRepository athleteLocationRepository;

	@Autowired
	UserGenderRepository athleteGenderRepository;

	@Autowired
	MatchNameRepository matchNameRepository;

	@Autowired
	ProfileImageRepository profileImageRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public Scouts addScout(Scouts scout, String userId) {

		if (scout == null || userId == null) {

			throw new NullPointerException("Have to give all the inputs properly....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (!user.getRoles().contains(Role.ROLE_ADMIN) && !user.getRoles().contains(Role.ROLE_SCOUT)) {

				throw new Exception();

			}

			Athelete athelete = atheleteRepository.findById(scout.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such valid user exist...");

		}

		try {

			if (scoutsRepository.findByAtheleteId(scout.getAtheleteId()) != null) {

				throw new NoSuchElementException("This athelete is already added as a scout...");

			}

		} catch (Exception e) {

			throw new NoSuchElementException(e.getMessage());

		}

		try {

			if (!scout.getMatches().isEmpty() || !scout.getEvents().isEmpty()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No scout can attend in any event in the creation time...");

		}

		scout = scoutsRepository.save(scout);

		if (scout == null) {

			return null;

		}

		return scout;

	}

	@Override
	public List<ScoutResponse> seeAllScouts() {

		return getScoutsResponseFromScoutList(scoutsRepository.findAll());

	}

	@Override
	public ScoutResponse findByAtheleteId(String atheleteId) {

		if (atheleteId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Scouts scout = scoutsRepository.findByAtheleteId(atheleteId);

			if (scout == null) {

				throw new NoSuchElementException("Failed to find such scout....");

			}

			return getScoutsResponseFromScout(scout);

		} catch (Exception e) {

			throw new NoSuchElementException("There is no scout with that athelete id...");

		}

	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public Scouts updateScouts(String scoutId, String userId, Scouts scout) {

		if (scout == null || userId == null) {

			throw new NullPointerException("Have to give all the inputs properly....");

		}

		try {

			User user = userRepository.findById(userId).get();

			Athelete athelete = atheleteRepository.findById(scout.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			if (!athelete.getUserId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such valid user exist...");

		}

		try {

			if (scoutsRepository.findByAtheleteId(scout.getAtheleteId()) == null) {

				throw new NoSuchElementException("This athelete is already added as a scout...");

			}

			scout.setId(scoutsRepository.findByAtheleteId(scout.getAtheleteId()).getId());

		} catch (Exception e) {

			throw new NoSuchElementException(e.getMessage());

		}

		try {

			if (!scout.getMatches().isEmpty()) {

				for (String i : scout.getMatches()) {

					Match match = matchRepository.findById(i).get();

					if (match == null) {

						throw new Exception();

					}

					List<String> list = match.getTeams();

					boolean find = false;

					for (String j : list) {

						Team team = teamRepository.findById(j).get();

						if (team == null) {

							throw new Exception();

						}

						if (team.getScouts().equals(scout.getId())) {

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

			throw new ArithmeticException("No scout can attend in any event in the creation time...");

		}

		try {

			if (!scout.getEvents().isEmpty()) {

				for (String i : scout.getEvents()) {

					Match match = matchRepository.findById(i).get();

					if (match == null) {

						throw new Exception();

					}

					List<String> list = match.getTeams();

					boolean find = false;

					for (String j : list) {

						Team team = teamRepository.findById(j).get();

						if (team == null) {

							throw new Exception();

						}

						if (team.getScouts().equals(scout.getId())) {

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

			throw new ArithmeticException("No scout can attend in any event in the creation time...");

		}

		scout = scoutsRepository.save(scout);

		if (scout == null) {

			return null;

		}

		return scout;

	}

	@Override
	public boolean deleteScouts(String scoutId, String userId) {

		if (scoutId == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			Athelete _athlete = atheleteRepository.findByUserId(user.getId()).get();

			Scouts _scout = scoutsRepository.findByAtheleteId(_athlete.getId());

			if (_scout == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_ADMIN)) {

				Athelete athelete = atheleteRepository.findById(_scout.getAtheleteId()).get();

				if (!user.getRoles().contains(Role.ROLE_ADMIN) && !athelete.getUserId().equals(userId)) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such valid user exist...");

		}

		long count = scoutsRepository.count();

		cleaner.removeScout(scoutId);

		boolean yes = count != scoutsRepository.count();

		try {

			if (yes) {

				Team team = teamRepository.findByScoutsContainingIgnoreCase(scoutId);

				if (team != null) {

					team.getScouts().remove(scoutId);

					teamRepository.save(team);

				}

			}

		} catch (Exception e) {

		}

		return yes;

	}

	@Override
	public List<ScoutResponse> findByEventsContainingIgnoreCase(String eventId) {

		if (eventId == null) {

			throw new NullPointerException("False request...");

		}

		List<Scouts> list = scoutsRepository.findByEventsContainingIgnoreCase(eventId);

		if (list == null) {

			throw new NoSuchElementException("No scouts will participate such event...");

		}

		return getScoutsResponseFromScoutList(list);
	}

	@Override
	public List<ScoutResponse> findByMatchesContainingIgnoreCase(String eventId) {

		if (eventId == null) {

			throw new NullPointerException("False request...");

		}

		List<Scouts> list = scoutsRepository.findByMatchesContainingIgnoreCase(eventId);

		if (list == null) {

			throw new NoSuchElementException("No scouts will participate such event...");

		}

		return getScoutsResponseFromScoutList(list);
	}

	@Override
	public ScoutResponse findByScoutsId(String scoutId) {

		if (scoutId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			return getScoutsResponseFromScout(scoutsRepository.findById(scoutId).get());

		} catch (Exception e) {

			throw new NoSuchElementException(e.getMessage());

		}

	}

	@Override
	public List<ScoutResponse> findByAthleteClassification(AthleteClassificationTypes athleteClassificationTypes) {

		if (athleteClassificationTypes == null) {

			throw new NullPointerException("False request...");

		}

		List<Scouts> list = new ArrayList<>();

		try {

			List<AthleteClassification> athletes = athleteClassificationRepository
					.findByAthleteClassificationTypes(athleteClassificationTypes);

			if (athletes.isEmpty()) {

				throw new Exception();

			}

			List<String> allAthleteId = athletes.stream().map(AthleteClassification::getAthleteId)
					.collect(Collectors.toList());

			list = scoutsRepository.findByAtheleteIdIn(allAthleteId);

			if (list.isEmpty()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such scouts find at here...");

		}

		return getScoutsResponseFromScoutList(list);
	}

	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private ScoutResponse getScoutsResponseFromScout(Scouts scout) {

		List<Scouts> list = new ArrayList<>();

		list.add(scout);

		return getScoutsResponseFromScoutList(list).get(0);

	}

	private List<ScoutResponse> getScoutsResponseFromScoutList(List<Scouts> scouts) {

		List<ScoutResponse> responses = new ArrayList<>();

		List<String> athleteIds = scouts.stream().filter(Objects::nonNull)
				.filter(scout -> scout.getAtheleteId() != null).map(Scouts::getAtheleteId).collect(Collectors.toList());

		List<Athelete> athletes = atheleteRepository.findAllById(athleteIds).stream().filter(Objects::nonNull)
				.collect(Collectors.toList());

		CompletableFuture<Map<String, Athelete>> athleteFuture = CompletableFuture
				.supplyAsync(() -> athletes.isEmpty() ? new HashMap<>()
						: athletes.stream().filter(Objects::nonNull)
								.collect(Collectors.toMap(Athelete::getId, Function.identity())),
						executor);

		List<String> userIds = athletes.stream().map(Athelete::getUserId).collect(Collectors.toList());

		// 🔥 CORRECT: flatMap on a List of Lists
		List<String> allEventAttendanceIds = athletes.stream().filter(athlete -> athlete.getEventAttendence() != null) // Avoid
																														// null
				.flatMap(athlete -> athlete.getEventAttendence().stream()) // Convert each List to Stream
				.distinct() // Remove duplicates
				.collect(Collectors.toList());

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

		CompletableFuture<Map<String, ScoutClassification>> scoutClassificationFuture = CompletableFuture
				.supplyAsync(
						() -> scoutClassificationRepository.findAll()
								.isEmpty()
										? new HashMap<>()
										: scoutClassificationRepository.findAll().stream().filter(Objects::nonNull)
												.filter(scoutClassification -> scoutClassification.getScoutId() != null)
												.collect(Collectors.toMap(ScoutClassification::getScoutId,
														Function.identity(), (existing, replacement) -> existing)),
						executor);

		CompletableFuture.allOf(athleteFuture, userFuture, locationFuture, genderFuture, classificationFuture,
				matchNameFuture, profileImageFuture, scoutClassificationFuture).join();

		Map<String, Athelete> athleteMap = athleteFuture.join();
		Map<String, User> userMap = userFuture.join();
		Map<String, AthleteLocation> locationMap = locationFuture.join();
		Map<String, UserGender> genderMap = genderFuture.join();
		Map<String, AthleteClassification> classificationMap = classificationFuture.join();
		Map<String, String> matchNameMap = matchNameFuture.join();
		Map<String, ScoutClassification> scoutClassificationMap = scoutClassificationFuture.join();
		Map<String, ProfileIamge> profileImageMap = profileImageFuture.join();

		for (Scouts scout : scouts) {

			try {

				ScoutResponse response = new ScoutResponse();

				try {

					Athelete athlete = athleteMap.get(scout.getAtheleteId());

					try {

						response.setAtheleteId(athlete.getId());
						response.setAge(athlete.getAge());
						response.setHeight(athlete.getHeight());
						response.setWeight(athlete.getWeight());
						response.setUserId(athlete.getUserId());
						response.setPosition(athlete.getPosition());
						response.setPresentAthleteTeam(athlete.getPresentTeam());

						try {

							response.setGameLogs(athlete.getGameLogs());

						} catch (Exception e) {

						}

						try {

							response.setEventAttendence(athlete.getEventAttendence());

						} catch (Exception e) {

						}

						try {

							response.setHighlightReels(athlete.getHighlightReels());

						} catch (Exception e) {

						}

					} catch (Exception e) {

					}

					try {

						List<String> matchNames = athlete.getEventAttendence() != null
								? athlete.getEventAttendence().stream().filter(Objects::nonNull)
										.map(matchId -> matchNameMap.get(matchId)).collect(Collectors.toList())
								: new ArrayList<>();

						response.setEventNames(matchNames);

					} catch (Exception e) {

					}

					try {

						User user = userMap.get(athlete.getUserId());
						if (user != null) {
							response.setName(user.getName());
							response.setEmail(user.getEmail());
							response.setRoles(user.getRoles());
						}

					} catch (Exception e) {

					}

					try {

						AthleteLocation loc = locationMap.get(athlete.getId());
						if (loc != null) {
							response.setLattitude(loc.getLattitude());
							response.setLongitude(loc.getLongitude());
							response.setLocationName(loc.getLocationName());
							response.setLocationId(loc.getId());
						}

					} catch (Exception e) {

					}

					try {

						UserGender gender = genderMap.get(athlete.getUserId());
						if (gender != null) {
							response.setGender(gender.getGender());
							response.setUserGenderId(gender.getId());
						}

					} catch (Exception e) {

					}

					try {

						AthleteClassification cls = classificationMap.get(athlete.getId());
						if (cls != null) {
							response.setAthleteClassificationId(cls.getId());
							response.setAthleteClassificationTypes(cls.getAthleteClassificationTypes());
						}

					} catch (Exception e) {

					}

					try {

						if (profileImageMap.containsKey(athlete.getUserId())) {

							response.setImageHex(profileImageMap.get(athlete.getUserId()).getImageHex());

						}

					} catch (Exception e) {

					}

				} catch (Exception e) {

				}

				try {

					List<String> scoutsMatchNames = scout.getMatches() != null
							? scout.getMatches().stream().filter(Objects::nonNull)
									.map(matchId -> matchNameMap.get(matchId)).collect(Collectors.toList())
							: new ArrayList<>();

					response.setScoutsMatches(scout.getMatches());
					response.setScoutsMatchNames(scoutsMatchNames);

				} catch (Exception e) {

				}

				try {

					List<String> scoutsEventNames = scout.getEvents() != null
							? scout.getEvents().stream().filter(Objects::nonNull)
									.map(matchId -> matchNameMap.get(matchId)).collect(Collectors.toList())
							: new ArrayList<>();

					response.setScoutsEvents(scout.getEvents());
					response.setScoutsEventNames(scoutsEventNames);

				} catch (Exception e) {

				}

				try {

					response.setId(scout.getId());

				} catch (Exception e) {

				}

				try {

					ScoutClassification classification = scoutClassificationMap.get(scout.getId());

					response.setScoutClassificationId(classification.getId());
					response.setScoutClassificationTypes(classification.getScoutClassificationTypes());

				} catch (Exception e) {

				}

				responses.add(response);

			} catch (Exception e) {

			}

		}

		return responses;

	}

	@Override
	public ScoutsListResponseDTO findAllScoutsPaginated(Pageable pageable, String baseUrl) {
		List<Scouts> allScouts = scoutsRepository.findAll(pageable).getContent();
		List<ScoutResponse> allResponses = getScoutsResponseFromScoutList(allScouts);

		return getPaginatedResponse(allResponses, pageable, baseUrl, null, null, null, null, null);
	}

	@Override
	public ScoutsListResponseDTO searchByAtheleteIdPaginated(String atheleteId, Pageable pageable, String baseUrl) {
		try {
			Scouts scout = scoutsRepository.findByAtheleteId(atheleteId);
			List<ScoutResponse> responses = new ArrayList<>();
			if (scout != null) {
				responses.add(getScoutsResponseFromScout(scout));
			}
			return getPaginatedResponse(responses, pageable, baseUrl, "atheleteId", atheleteId, null, null, null);
		} catch (Exception e) {
			return getPaginatedResponse(new ArrayList<>(), pageable, baseUrl, "atheleteId", atheleteId, null, null,
					null);
		}
	}

	@Override
	public ScoutsListResponseDTO searchByScoutsIdPaginated(String scoutId, Pageable pageable, String baseUrl) {
		try {
			Scouts scout = scoutsRepository.findById(scoutId).orElse(null);
			List<ScoutResponse> responses = new ArrayList<>();
			if (scout != null) {
				responses.add(getScoutsResponseFromScout(scout));
			}
			return getPaginatedResponse(responses, pageable, baseUrl, "scoutId", scoutId, null, null, null);
		} catch (Exception e) {
			return getPaginatedResponse(new ArrayList<>(), pageable, baseUrl, "scoutId", scoutId, null, null, null);
		}
	}

	@Override
	public ScoutsListResponseDTO searchByEventsContainingPaginated(String eventId, Pageable pageable, String baseUrl) {
		List<Scouts> scouts = scoutsRepository.findByEventsContainingIgnoreCase(eventId, pageable).getContent();
		List<ScoutResponse> responses = getScoutsResponseFromScoutList(scouts);
		return getPaginatedResponse(responses, pageable, baseUrl, "eventId", eventId, null, null, null);
	}

	@Override
	public ScoutsListResponseDTO searchByMatchesContainingPaginated(String matchId, Pageable pageable, String baseUrl) {
		List<Scouts> scouts = scoutsRepository.findByMatchesContainingIgnoreCase(matchId, pageable).getContent();
		List<ScoutResponse> responses = getScoutsResponseFromScoutList(scouts);
		return getPaginatedResponse(responses, pageable, baseUrl, "matchId", matchId, null, null, null);
	}

	@Override
	public ScoutsListResponseDTO searchByClassificationPaginated(AthleteClassificationTypes classification,
			Pageable pageable, String baseUrl) {
		List<ScoutResponse> allResponses = findByAthleteClassification(classification);
		return getPaginatedResponse(allResponses, pageable, baseUrl, "classification", classification.toString(), null,
				null, null);
	}

	@Override
	public ScoutsListResponseDTO searchByNamePaginated(String name, Pageable pageable, String baseUrl) {
		// First get all scouts
		List<Scouts> allScouts = scoutsRepository.findAll();
		List<ScoutResponse> allResponses = getScoutsResponseFromScoutList(allScouts);

		// Filter by name
		List<ScoutResponse> filteredResponses = allResponses.stream().filter(
				response -> response.getName() != null && response.getName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());

		return getPaginatedResponse(filteredResponses, pageable, baseUrl, "name", name, null, null, null);
	}

	// HELPER METHOD: Pagination logic
	private ScoutsListResponseDTO getPaginatedResponse(List<ScoutResponse> allResponses, Pageable pageable,
			String baseUrl, String searchType, String searchValue, String classification, String eventId,
			String matchId) {

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();

		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageSize, allResponses.size());

		List<ScoutResponse> pageContent = start < allResponses.size() ? allResponses.subList(start, end)
				: new ArrayList<>();

		Page<ScoutResponse> page = new PageImpl<>(pageContent, pageable, allResponses.size());

		ScoutsListResponseDTO response = new ScoutsListResponseDTO(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), page.getTotalPages(), page.isLast(), page.isFirst(),
				page.getNumberOfElements());

		// Build navigation links
		String searchParams = buildSearchParams(searchType, searchValue, classification, eventId, matchId);

		if (page.hasPrevious()) {
			response.setPrevLink(buildUrl(baseUrl, currentPage - 1, pageSize, searchParams));
		}
		if (page.hasNext()) {
			response.setNextLink(buildUrl(baseUrl, currentPage + 1, pageSize, searchParams));
		}
		response.setFirstLink(buildUrl(baseUrl, 0, pageSize, searchParams));
		response.setLastLink(buildUrl(baseUrl, page.getTotalPages() - 1, pageSize, searchParams));

		return response;
	}

	private String buildSearchParams(String searchType, String searchValue, String classification, String eventId,
			String matchId) {
		StringBuilder params = new StringBuilder();
		if (searchType != null && searchValue != null) {
			params.append("&").append(searchType).append("=").append(searchValue);
		}
		if (classification != null) {
			params.append("&classification=").append(classification);
		}
		if (eventId != null) {
			params.append("&eventId=").append(eventId);
		}
		if (matchId != null) {
			params.append("&matchId=").append(matchId);
		}
		return params.toString();
	}

	private String buildUrl(String baseUrl, int page, int size, String additionalParams) {
		return baseUrl + "?page=" + page + "&size=" + size + additionalParams;
	}

}

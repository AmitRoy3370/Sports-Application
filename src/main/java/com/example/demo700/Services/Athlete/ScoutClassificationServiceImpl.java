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
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.CoachListResponseDTO;
import com.example.demo700.DTOFiles.ScoutClassificationListResonseDTO;
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
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Models.FileUploadModel.ProfileIamge;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.ScoutClassificationRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;

@Service
public class ScoutClassificationServiceImpl implements ScoutClassificationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGenderRepository athleteGenderRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private AthleteLocationRepository athleteLocationRepository;

	@Autowired
	private AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	private ScoutsRepository scoutRepository;

	@Autowired
	private MatchNameRepository matchNameRepository;

	@Autowired
	private ProfileImageRepository profileImageRepository;

	@Autowired
	private ScoutClassificationRepository scoutClassificationRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public ScoutClassification addAthleteClassification(ScoutClassification scoutClassification, String userId) {

		if (scoutClassification == null || userId == null) {

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

			Scouts scout = scoutRepository.findByAtheleteId(athlete.getId());

			if (scout == null) {

				throw new Exception();

			}

			if (!scout.getId().equals(scoutClassification.getScoutId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to add classification...");

		}

		scoutClassification = scoutClassificationRepository.save(scoutClassification);

		if (scoutClassification == null) {

			throw new ArithmeticException("Scouts classification is not set....");

		}

		return scoutClassification;
	}

	@Override
	public ScoutClassification updateAthleteClassification(ScoutClassification scoutClassification, String userId,
			String scoutClassificationId) {

		if (scoutClassification == null || userId == null || scoutClassificationId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			ScoutClassification classification = scoutClassificationRepository.findById(scoutClassificationId).get();

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

			Scouts scout = scoutRepository.findByAtheleteId(athlete.getId());

			if (scout == null) {

				throw new Exception();

			}

			if (!scout.getId().equals(classification.getScoutId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here..");

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

			Scouts scout = scoutRepository.findByAtheleteId(athlete.getId());

			if (scout == null) {

				throw new Exception();

			}

			if (!scout.getId().equals(scoutClassification.getScoutId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to add classification...");

		}

		scoutClassification.setId(scoutClassificationId);

		scoutClassification = scoutClassificationRepository.save(scoutClassification);

		if (scoutClassification == null) {

			throw new ArithmeticException("Scouts classification is not updated....");

		}

		return scoutClassification;
	}

	@Override
	public ScoutClassificationListResonseDTO seeAll(int page, int size, String requestUrl) {

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<ScoutClassification> classifications = scoutClassificationRepository.findAll(pageable);

			if (!classifications.hasContent()) {

				throw new Exception();

			}

			List<ScoutClassification> list = classifications.getContent();

			List<String> allScoutId = list.stream().map(ScoutClassification::getScoutId).collect(Collectors.toList());

			List<Scouts> allScouts = scoutRepository.findAllById(allScoutId);

			List<ScoutResponse> scoutResponses = getScoutsResponseFromScoutList(allScouts);

			ScoutClassificationListResonseDTO response = new ScoutClassificationListResonseDTO(scoutResponses,
					classifications.getNumber(), classifications.getSize(), classifications.getTotalElements(),
					classifications.getTotalPages());

			// Build navigation links
			String baseUrl = requestUrl.split("\\?")[0];
			response.setSelfLink(buildUrl(baseUrl, page, size, ""));

			if (classifications.hasNext()) {
				response.setNextLink(buildUrl(baseUrl, page + 1, size, ""));
			}
			if (classifications.hasPrevious()) {
				response.setPrevLink(buildUrl(baseUrl, page - 1, size, ""));
			}
			if (classifications.getTotalPages() > 0) {
				response.setFirstLink(buildUrl(baseUrl, 0, size, ""));
				response.setLastLink(buildUrl(baseUrl, classifications.getTotalPages() - 1, size, ""));
			}

			response.setCurrentUrl(requestUrl);
			response.setMessage("Found " + scoutResponses.size());

			return response;

		} catch (Exception e) {

			throw new NoSuchElementException("No such classiifcation find at here...");

		}

	}

	@Override
	public ScoutResponse findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			ScoutClassification classification = scoutClassificationRepository.findById(id).get();

			if (classification == null) {

				throw new Exception();

			}

			Scouts scout = scoutRepository.findById(classification.getScoutId()).get();

			return getScoutsResponseFromScout(scout);

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here...");

		}

	}

	@Override
	public ScoutResponse findByCoachId(String scoutId) {

		if (scoutId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			ScoutClassification classification = scoutClassificationRepository.findByScoutId(scoutId);

			if (classification == null) {

				throw new Exception();

			}

			Scouts scout = scoutRepository.findById(classification.getScoutId()).get();

			return getScoutsResponseFromScout(scout);

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here...");

		}

	}

	@Override
	public ScoutClassificationListResonseDTO findByAthleteClassificationTypes(
			AthleteClassificationTypes athleteClassificationTypes, int page, int size, String requestUrl) {

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<ScoutClassification> classifications = scoutClassificationRepository
					.findByScoutClassificationTypes(athleteClassificationTypes, pageable);

			if (!classifications.hasContent()) {

				throw new Exception();

			}

			List<ScoutClassification> list = classifications.getContent();

			List<String> allScoutId = list.stream().map(ScoutClassification::getScoutId).collect(Collectors.toList());

			List<Scouts> allScouts = scoutRepository.findAllById(allScoutId);

			List<ScoutResponse> scoutResponses = getScoutsResponseFromScoutList(allScouts);

			ScoutClassificationListResonseDTO response = new ScoutClassificationListResonseDTO(scoutResponses,
					classifications.getNumber(), classifications.getSize(), classifications.getTotalElements(),
					classifications.getTotalPages());

			// Build navigation links
			String baseUrl = requestUrl.split("\\?")[0];
			response.setSelfLink(buildUrl(baseUrl, page, size, ""));

			if (classifications.hasNext()) {
				response.setNextLink(buildUrl(baseUrl, page + 1, size, ""));
			}
			if (classifications.hasPrevious()) {
				response.setPrevLink(buildUrl(baseUrl, page - 1, size, ""));
			}
			if (classifications.getTotalPages() > 0) {
				response.setFirstLink(buildUrl(baseUrl, 0, size, ""));
				response.setLastLink(buildUrl(baseUrl, classifications.getTotalPages() - 1, size, ""));
			}

			response.setCurrentUrl(requestUrl);
			response.setMessage("Found " + scoutResponses.size());

			return response;

		} catch (Exception e) {

			throw new NoSuchElementException("No such classiifcation find at here...");

		}
	}

	@Override
	public boolean removeAthleteClassification(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			ScoutClassification classification = scoutClassificationRepository.findById(id).get();

			if (classification == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = scoutClassificationRepository.count();

				cleaner.removeScoutClassification(id);

				return count != scoutClassificationRepository.count();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			Scouts scout = scoutRepository.findByAtheleteId(athlete.getId());

			if (scout == null) {

				throw new Exception();

			}

			if (!scout.getId().equals(classification.getScoutId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here..");

		}

		long count = scoutClassificationRepository.count();

		cleaner.removeScoutClassification(id);

		return count != scoutClassificationRepository.count();

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

		List<Athelete> athletes = athleteRepository.findAllById(athleteIds).stream().filter(Objects::nonNull)
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

		CompletableFuture.allOf(athleteFuture, userFuture, locationFuture, genderFuture, classificationFuture,
				matchNameFuture, profileImageFuture).join();

		Map<String, Athelete> athleteMap = athleteFuture.join();
		Map<String, User> userMap = userFuture.join();
		Map<String, AthleteLocation> locationMap = locationFuture.join();
		Map<String, UserGender> genderMap = genderFuture.join();
		Map<String, AthleteClassification> classificationMap = classificationFuture.join();
		Map<String, String> matchNameMap = matchNameFuture.join();
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

				responses.add(response);

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

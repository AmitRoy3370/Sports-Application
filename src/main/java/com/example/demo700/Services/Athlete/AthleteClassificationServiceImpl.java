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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.ENUMS.Role;
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
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;
import com.example.demo700.Validator.URLValidator;

@Service
public class AthleteClassificationServiceImpl implements AthleteClassificationService {

	@Autowired
	private AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	AthleteLocationRepository athleteLocationRepository;

	@Autowired
	UserGenderRepository athleteGenderRepository;

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
	private CyclicCleaner cleaner;

	// Optimized pagination settings for 50TB database
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 50; // Reduced from 100 to prevent memory issues
	private static final int MAX_BATCH_SIZE = 200; // Reduced from 500 for better memory management

	@Override
	public AthleteClassification addAthleteClassification(AthleteClassification athleteClassification, String userId) {

		if (athleteClassification == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(athleteClassification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository
					.findByAthleteId(athleteClassification.getAthleteId());

			if (classification != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This athlete clasification is already added at here...");

		} catch (Exception e) {

		}

		athleteClassification = athleteClassificationRepository.save(athleteClassification);

		if (athleteClassification == null) {

			throw new ArithmeticException("No such athlete classification add at here...");

		}

		return athleteClassification;
	}

	@Override
	public AthleteClassification updateAthleteClassification(AthleteClassification athleteClassification, String userId,
			String athleteClassificationId) {

		if (athleteClassification == null || userId == null || athleteClassificationId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository.findById(athleteClassificationId)
					.get();

			if (classification == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(classification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(athleteClassification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository
					.findByAthleteId(athleteClassification.getAthleteId());

			if (classification != null) {

				if (!classification.getId().equals(athleteClassificationId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This athlete clasification is already added at here...");

		} catch (Exception e) {

		}

		athleteClassification.setId(athleteClassificationId);

		athleteClassification = athleteClassificationRepository.save(athleteClassification);

		if (athleteClassification == null) {

			throw new ArithmeticException("No such athlete classification add at here...");

		}

		return athleteClassification;
	}

	@Override
	public AthleteListResponseDTO seeAll(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "_id"));

		Page<AthleteClassification> list = athleteClassificationRepository.findAll(pageable);

		if (!list.hasContent()) {

			throw new NoSuchElementException("No such classification added at here....");

		}

		List<String> athletesId = list.getContent().stream().map(AthleteClassification::getAthleteId).distinct()
				.collect(Collectors.toList());

		List<Athelete> athletes = athleteRepository.findAllById(athletesId);

		List<AthleteRequestDTO> detailsAthlete = getListDetailsFromAthleteList(athletes);

		return new AthleteListResponseDTO(detailsAthlete, page, size, list.getTotalElements(), list.getTotalPages());
	}

	@Override
	public AthleteRequestDTO findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			AthleteClassification athleteClassification = athleteClassificationRepository.findById(id).get();

			if (athleteClassification == null) {

				throw new Exception();

			}

			return getDetailsFromAthleteId(athleteClassification.getAthleteId());

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification add at here...");

		}
	}

	@Override
	public AthleteRequestDTO findByAthleteId(String athleteId) {

		if (athleteId == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			AthleteClassification athleteClassification = athleteClassificationRepository.findByAthleteId(athleteId);

			if (athleteClassification == null) {

				throw new Exception();

			}

			return getDetailsFromAthleteId(athleteClassification.getAthleteId());

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification add at here...");

		}
	}

	@Override
	public AthleteListResponseDTO findByAthleteClassificationTypes(
			AthleteClassificationTypes athleteClassificationTypes, int page, int size) {

		if (athleteClassificationTypes == null) {

			throw new NullPointerException("False request....");

		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "_id"));

		Page<AthleteClassification> list = athleteClassificationRepository
				.findByAthleteClassificationTypes(athleteClassificationTypes, pageable);

		if (!list.hasContent()) {

			throw new NoSuchElementException("No such classification added at here....");

		}

		List<String> athletesId = list.getContent().stream().map(AthleteClassification::getAthleteId).distinct()
				.collect(Collectors.toList());

		List<Athelete> athletes = athleteRepository.findAllById(athletesId);

		List<AthleteRequestDTO> detailsAthlete = getListDetailsFromAthleteList(athletes);

		return new AthleteListResponseDTO(detailsAthlete, page, size, list.getTotalElements(), list.getTotalPages());
	}

	@Override
	public boolean removeAthleteClassification(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository.findById(id).get();

			if (classification == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(classification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = athleteClassificationRepository.count();

				cleaner.removeAthleteClassification(id);

				return count != athleteClassificationRepository.count();

			}

			if (!athlete.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here...");

		}

		long count = athleteClassificationRepository.count();

		cleaner.removeAthleteClassification(id);

		return count != athleteClassificationRepository.count();
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

}

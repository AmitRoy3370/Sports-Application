package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
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

		List<Athelete> athletes = new ArrayList<>();

		for (AthleteClassification i : list) {

			Athelete athlete = athleteRepository.findById(i.getAthleteId()).get();

			athletes.add(athlete);

		}

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

		List<Athelete> athletes = new ArrayList<>();

		for (AthleteClassification i : list) {

			Athelete athlete = athleteRepository.findById(i.getAthleteId()).get();

			athletes.add(athlete);

		}
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

	// 🔥 OPTIMIZED BATCH FETCHING for 50TB database
	public List<AthleteRequestDTO> getListDetailsFromAthleteList(List<Athelete> athletes) {
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

		// Batch fetch with error handling
		Map<String, User> userMap = new HashMap<>();
		Map<String, AthleteLocation> locationMap = new HashMap<>();
		Map<String, UserGender> genderMap = new HashMap<>();
		Map<String, AthleteClassification> classificationMap = new HashMap<>();

		try {
			List<User> users = userRepository.findAllById(userIds);
			for (User u : users) {
				userMap.put(u.getId(), u);
			}
		} catch (Exception e) {
			System.err.println("Error fetching users: " + e.getMessage());
		}

		try {
			List<AthleteLocation> locations = athleteLocationRepository.findByAthleteIdIn(athleteIds);
			for (AthleteLocation l : locations) {
				locationMap.put(l.getAthleteId(), l);
			}
		} catch (Exception e) {
			System.err.println("Error fetching locations: " + e.getMessage());
		}

		try {
			List<UserGender> genders = athleteGenderRepository.findByUserIdIn(userIds);
			for (UserGender g : genders) {
				genderMap.put(g.getUserId(), g);
			}
		} catch (Exception e) {
			System.err.println("Error fetching genders: " + e.getMessage());
		}

		try {
			List<AthleteClassification> classifications = athleteClassificationRepository.findByAthleteIdIn(athleteIds);
			for (AthleteClassification c : classifications) {
				classificationMap.put(c.getAthleteId(), c);
			}
		} catch (Exception e) {
			System.err.println("Error fetching classifications: " + e.getMessage());
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
				}

				UserGender gender = genderMap.get(a.getUserId());
				if (gender != null) {
					dto.setGender(gender.getGender());
				}

				AthleteClassification cls = classificationMap.get(a.getId());
				if (cls != null) {
					dto.setAthleteClassificationTypes(cls.getAthleteClassificationTypes());
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

		AthleteRequestDTO athlete = new AthleteRequestDTO();

		athlete.setId(athleteDetails.getId());
		athlete.setAge(athleteDetails.getAge());
		athlete.setGameLogs(athleteDetails.getGameLogs());
		athlete.setEventAttendence(athleteDetails.getEventAttendence());
		athlete.setHeight(athleteDetails.getHeight());
		athlete.setWeight(athleteDetails.getWeight());
		athlete.setUserId(athleteDetails.getUserId());
		athlete.setPosition(athleteDetails.getPosition());
		athlete.setPresentTeam(athleteDetails.getPresentTeam());
		athlete.setHighlightReels(athleteDetails.getHighlightReels());

		User user = userRepository.findById(athleteDetails.getUserId())
				.orElseThrow(() -> new NoSuchElementException("User not found"));

		athlete.setName(user.getName());
		athlete.setEmail(user.getEmail());
		athlete.setRoles(user.getRoles());

		try {
			AthleteLocation location = athleteLocationRepository.findByAthleteId(athleteId);
			if (location != null) {
				// AthleteLocation location = locationOpt.get();
				athlete.setLattitude(location.getLattitude());
				athlete.setLongitude(location.getLongitude());
				athlete.setLocationName(location.getLocationName());
			}
		} catch (Exception e) {
			// Location not found
		}

		try {
			UserGender genderOpt = athleteGenderRepository.findByUserId(athleteDetails.getUserId());
			if (genderOpt != null) {
				athlete.setGender(genderOpt.getGender());
			}
		} catch (Exception e) {
			// Gender not found
		}

		try {
			AthleteClassification classificationOpt = athleteClassificationRepository.findByAthleteId(athleteId);
			if (classificationOpt != null) {
				athlete.setAthleteClassificationTypes(classificationOpt.getAthleteClassificationTypes());
			}
		} catch (Exception e) {
			// Classification not found
		}

		return athlete;
	}

}

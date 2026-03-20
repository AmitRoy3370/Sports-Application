package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
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

	URLValidator urlValidator = new URLValidator();

	@Autowired
	CyclicCleaner cleaner;

	@Override
	public Athelete addAthelete(Athelete athelete, String userId) {

		if (athelete == null || userId == null) {

			throw new NullPointerException("Have to take input of all data...");

		}

		User _user = userRepository.findById(userId).get();

		if (_user == null) {

			throw new ArithmeticException("False user's request...");

		}

		if (!_user.getRoles().contains(Role.ROLE_ADMIN) && !_user.getRoles().contains(Role.ROLE_ATHLETE)) {

			throw new ArithmeticException("Only admin can add a player...");

		}

		System.out.println(athelete.toString());

		User user = userRepository.findById(athelete.getUserId()).get();

		if (user == null) {

			throw new ArithmeticException("Wrong user id...");

		}

		if (!user.getRoles().contains(Role.ROLE_ATHLETE)) {

			throw new ArithmeticException("False user request...");

		}

		try {

			Athelete _athelete = athleteRepository.findByUserId(athelete.getUserId()).get();

			if (_athelete != null) {

				return null;

			}

		} catch (Exception e) {

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

			if (athelete.getPresentTeam() != null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No athlete can join a team in time of creation...");

		}

		try {

			if (!athelete.getEventAttendence().isEmpty()) {

				throw new Exception();

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

		athelete = athleteRepository.save(athelete);

		if (athelete == null) {

			return null;

		}

		return athelete;
	}

	@Override
	public List<AthleteRequestDTO> seeAll() {

		List<Athelete> _list = athleteRepository.findAll();

		if (_list.isEmpty()) {

			return null;

		}

		List<AthleteRequestDTO> list = new ArrayList<>();

		for (Athelete i : _list) {

			list.add(getDetailsFromAthleteId(i.getId()));

		}

		return list;
	}

	@Override
	public Athelete updateAthelete(Athelete athelete, String userId, String atheleteId) {

		if (athelete == null || userId == null || atheleteId == null) {

			throw new NullPointerException("Have to take input of all data...");

		}

		Athelete _athelete = athleteRepository.findById(atheleteId).get();

		if (_athelete == null) {

			throw new ArithmeticException("No athelete present to update...");

		}

		User _user = userRepository.findById(userId).get();

		if (_user == null) {

			throw new ArithmeticException("False user's request...");

		}

		if (!_user.getRoles().contains(Role.ROLE_ATHLETE) || !_user.getId().equals(_athelete.getUserId())) {

			throw new ArithmeticException("Only athelete can update ownself...");

		}

		System.out.println(athelete.toString());

		User user = userRepository.findById(athelete.getUserId()).get();

		if (user == null) {

			throw new ArithmeticException("Wrong user id...");

		}

		Athelete __athlete = athleteRepository.findByUserId(user.getId()).get();

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

		try {

			Athelete __athelete = athleteRepository.findByUserId(athelete.getId()).get();

			if (__athelete != null) {

				return null;

			}

		} catch (Exception e) {

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

			if (!athelete.getEventAttendence().isEmpty()) {

				for (String i : athelete.getEventAttendence()) {

					Match match = matchRepository.findById(i).get();

					if (match == null) {

						throw new Exception();

					}

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

		try {

			Athelete athelete = athleteRepository.findById(atheleteId).get();

			if (athelete == null) {

				throw new ArithmeticException();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such athelete present at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new ArithmeticException();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such user present at here...");

		}

		if (userRepository.findById(userId).get().getRoles().contains(Role.ROLE_ADMIN)
				|| athleteRepository.findByUserId(userId).equals(atheleteId)) {

			long count = athleteRepository.count();

			cleaner.removeAthelete(atheleteId);

			boolean yes = count != athleteRepository.count();

			if (yes) {

				try {

					Team team = teamRepository.findByAtheletesContainingIgnoreCase(atheleteId);

					if (team != null) {

						team.getAtheletes().remove(atheleteId);

					}

				} catch (Exception e) {

				}

			}

			return yes;

		} else {

			return false;

		}

	}

	@Override
	public List<AthleteRequestDTO> searchAtheleteByTeamName(String teamName) {

		if (teamName == null) {

			throw new NullPointerException("No athelete find at here...");

		}

		List<Athelete> _list = athleteRepository.findBypresentTeam(teamName);

		List<AthleteRequestDTO> list = new ArrayList<>();

		for (Athelete i : _list) {

			list.add(getDetailsFromAthleteId(i.getId()));

		}

		return list;
	}

	@Override
	public List<AthleteRequestDTO> findByAgeLessThan(int age) {

		List<Athelete> _list = athleteRepository.findByAgeLessThan(age);

		List<AthleteRequestDTO> list = new ArrayList<>();

		for (Athelete i : _list) {

			list.add(getDetailsFromAthleteId(i.getId()));

		}

		return list;
	}

	@Override
	public List<AthleteRequestDTO> findByHeightGreaterThan(double height) {

		List<Athelete> _list = athleteRepository.findByHeightGreaterThan(height);

		List<AthleteRequestDTO> list = new ArrayList<>();

		for (Athelete i : _list) {

			list.add(getDetailsFromAthleteId(i.getId()));

		}

		return list;
	}

	@Override
	public List<AthleteRequestDTO> findByWeightLessThan(double weight) {
		List<Athelete> _list = athleteRepository.findByWeightLessThan(weight);

		List<AthleteRequestDTO> list = new ArrayList<>();

		for (Athelete i : _list) {

			list.add(getDetailsFromAthleteId(i.getId()));

		}

		return list;
	}

	@Override
	public List<AthleteRequestDTO> findByPresentTeamIgnoreCase(String teamName) {

		if (teamName == null) {
			throw new NullPointerException("Invalid request...");
		}

		List<Athelete> list = athleteRepository.findByPresentTeamIgnoreCase(teamName);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
	}

	@Override
	public List<AthleteRequestDTO> findByPosition(String position) {

		List<Athelete> list = athleteRepository.findByPositionContainingIgnoreCase(position);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
	}

	@Override
	public List<AthleteRequestDTO> findByEventAttendenceContainingIgnoreCase(String eventName) {

		if (eventName == null) {
			throw new NullPointerException("Invalid request...");
		}

		List<Athelete> list = athleteRepository.findByEventAttendenceContainingIgnoreCase(eventName);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
	}

	@Override
	public List<AthleteRequestDTO> findByGameLogsContainingIgnoreCase(String gameLog) {

		if (gameLog == null) {
			throw new NullPointerException("Invalid request...");
		}

		List<Athelete> list = athleteRepository.findByGameLogsContainingIgnoreCase(gameLog);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
	}

	@Override
	public List<AthleteRequestDTO> findByAgeLessThanAndHeightGreaterThan(int age, double height) {

		List<Athelete> list = athleteRepository.findByAgeLessThanAndHeightGreaterThan(age, height);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
	}

	@Override
	public List<AthleteRequestDTO> searchByTeamNamePartial(String partialName) {

		if (partialName == null) {
			throw new NullPointerException("Invalid request...");
		}

		List<Athelete> list = athleteRepository.searchByTeamNamePartial(partialName);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
	}

	@Override
	public List<AthleteRequestDTO> findByMultipleEvents(List<String> eventNames) {

		if (eventNames == null) {
			throw new NullPointerException("Invalid request...");
		}

		List<Athelete> list = athleteRepository.findByMultipleEvents(eventNames);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
	}

	@Override
	public List<AthleteRequestDTO> findByWeightRange(double min, double max) {

		List<Athelete> list = athleteRepository.findByWeightRange(min, max);

		if (list.isEmpty()) {
			throw new NoSuchElementException("No athletes found...");
		}

		List<AthleteRequestDTO> dtoList = new ArrayList<>();

		for (Athelete athlete : list) {
			dtoList.add(getDetailsFromAthleteId(athlete.getId()));
		}

		return dtoList;
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

		User user = userRepository.findById(actionUserId).get();

		if (user == null) {

			throw new ArithmeticException("unsafe operation...");

		}

		Athelete athelete = athleteRepository.findById(userId).get();

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

	private AthleteRequestDTO getDetailsFromAthleteId(String athleteId) {

		AthleteRequestDTO athlete = new AthleteRequestDTO();

		Athelete athleteDetails = athleteRepository.findById(athleteId).get();

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

		User user = userRepository.findById(athleteDetails.getUserId()).get();

		athlete.setName(user.getName());
		athlete.setEmail(user.getEmail());
		athlete.setRoles(user.getRoles());

		try {

			AthleteLocation location = athleteLocationRepository.findByAthleteId(athleteId);

			if (location != null) {

				athlete.setLattitude(location.getLattitude());
				athlete.setLongitude(location.getLongitude());
				athlete.setLocationName(location.getLocationName());

			}

		} catch (Exception e) {

		}

		try {

			UserGender userGender = athleteGenderRepository.findByUserId(athleteDetails.getUserId());

			if (userGender != null) {

				athlete.setGender(userGender.getGender());

			}

		} catch (Exception e) {

		}

		try {

			AthleteClassification classification = athleteClassificationRepository.findByAthleteId(athleteId);

			if (classification != null) {

				athlete.setAthleteClassificationTypes(classification.getAthleteClassificationTypes());

			}

		} catch (Exception e) {

		}

		return athlete;

	}

}

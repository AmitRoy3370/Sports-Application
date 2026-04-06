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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.TeamResponseDTO;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.DoctorRepositories.DoctorRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;

import jakarta.annotation.PreDestroy;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchNameRepository matchNameRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public Team addTeam(Team team, String userId) {

		if (team == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athelete = atheleteRepository.findByUserId(user.getId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelete.getId());

			if (teamOwner == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such user exist at here...");

		}

		TeamOwner teamOwner = null;

		try {

			teamOwner = teamOwnerRepository.findById(team.getTeamOwnerId()).get();

			if (teamOwner == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No valid team owner find at here..");

		}

		try {

			Team _team = teamRepository.findByTeamNameIgnoreCase(team.getTeamName());

			if (_team != null) {

				throw new ArithmeticException("Team name already exist...");

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException(e.getMessage());

		} catch (Exception e) {

		}

		try {

			for (String atheleteId : team.getAtheletes()) {

				Athelete athelete = atheleteRepository.findById(atheleteId)
						.orElseThrow(() -> new NoSuchElementException());

				if (athelete == null) {

					throw new NoSuchElementException();

				}

				Team _team = teamRepository.findByAtheletesContainingIgnoreCase(atheleteId);

				if (_team != null) {

					throw new NoSuchElementException();

				}

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException("In valid athelete request...");

		} catch (Exception e) {

		}

		try {

			for (String coachId : team.getCoaches()) {

				Coach _coach = coachRepository.findById(coachId).orElseThrow(() -> new NoSuchElementException());

				if (_coach == null) {

					throw new NoSuchElementException();

				}

				Team _team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

				if (_team != null) {

					throw new NoSuchElementException();

				}

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException("In valid coach request....");

		} catch (Exception e) {

		}

		try {

			for (String scoutId : team.getScouts()) {

				Scouts scout = scoutsRepository.findById(scoutId).orElseThrow(() -> new NoSuchElementException());

				if (scout == null) {

					throw new NoSuchElementException();

				}

				Team _team = teamRepository.findByScoutsContainingIgnoreCase(scoutId);

				if (_team != null) {

					throw new NoSuchElementException();

				}

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException("In valid scout request....");

		} catch (Exception e) {

		}

		try {

			for (String doctorId : team.getDoctors()) {

				Doctor doctor = doctorRepository.findById(doctorId).get();

				if (doctor == null) {

					throw new NoSuchElementException();

				}

				Team _team = teamRepository.findByDoctorsContainingIgnoreCase(doctor.getId());

				if (_team != null) {

					throw new NoSuchElementException();

				}

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException("In valid doctor request....");

		} catch (Exception e) {

		}

		if (!team.getMatches().isEmpty()) {

			throw new ArithmeticException("In the time of creation no team doesn't get any match...");

		}

		team = teamRepository.save(team);

		if (team == null) {

			return null;

		}

		if (teamOwner != null) {

			teamOwner.getTeams().add(team.getId());

			teamOwnerRepository.save(teamOwner);

		}

		return team;
	}

	@Override
	public List<TeamResponseDTO> seeAllTeam() {

		List<Team> list = teamRepository.findAll();

		return batchBuildTeamResponses(list);

	}

	@Override
	public Team updateTeam(Team team, String userId, String teamId) {

		if (team == null || userId == null || teamId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athelete = atheleteRepository.findByUserId(user.getId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelete.getId());

			if (teamOwner == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such user exist at here...");

		}

		try {

			Team _team = teamRepository.findById(teamId).get();

			if (_team == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("This team not exist at here..");

		}

		try {

			Team _team = teamRepository.findByTeamNameIgnoreCase(team.getTeamName());

			if (!_team.getId().equals(teamId)) {

				throw new ArithmeticException("Team name already exist...");

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException(e.getMessage());

		} catch (Exception e) {

		}

		try {

			for (String atheleteId : team.getAtheletes()) {

				Athelete athelete = atheleteRepository.findById(atheleteId)
						.orElseThrow(() -> new NoSuchElementException());

				if (athelete == null) {

					throw new NoSuchElementException();

				}

				Team _team = teamRepository.findByAtheletesContainingIgnoreCase(atheleteId);

				if (_team != null) {

					throw new NoSuchElementException();

				}

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException("In valid athelete request...");

		} catch (Exception e) {

		}

		try {

			for (String coachId : team.getCoaches()) {

				Coach _coach = coachRepository.findById(coachId).orElseThrow(() -> new NoSuchElementException());

				if (_coach == null) {

					throw new NoSuchElementException();

				}

				Team _team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

				if (_team != null) {

					throw new NoSuchElementException();

				}

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException("In valid coach request....");

		} catch (Exception e) {

		}

		try {

			for (String scoutId : team.getScouts()) {

				Scouts scout = scoutsRepository.findById(scoutId).orElseThrow(() -> new NoSuchElementException());

				if (scout == null) {

					throw new NoSuchElementException();

				}

				Team _team = teamRepository.findByScoutsContainingIgnoreCase(scoutId);

				if (_team != null) {

					throw new NoSuchElementException();

				}

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException("In valid scout request....");

		} catch (Exception e) {

		}

		if (!team.getMatches().isEmpty()) {

			for (String i : team.getMatches()) {

				try {

					Match match = matchRepository.findById(i).get();

					if (match == null) {

						throw new Exception();

					}

					if (!match.getTeams().contains(teamId)) {

						throw new Exception();

					}

				} catch (Exception e) {

					throw new NoSuchElementException("False team information...");

				}

			}

		}

		team.setId(teamId);

		team = teamRepository.save(team);

		if (team == null) {

			return null;

		}

		return team;
	}

	@Override
	public boolean deleteTeam(String teamId, String userId) {

		if (userId == null || teamId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = teamRepository.count();

				Team _team = teamRepository.findById(teamId).get();

				if (_team == null) {

					return false;

				} else {

					teamRepository.deleteById(teamId);

					return count != teamRepository.count();

				}

			}

			Athelete athelete = atheleteRepository.findByUserId(user.getId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelete.getId());

			if (teamOwner == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such user exist at here...");

		}

		try {

			Team _team = teamRepository.findById(teamId).get();

			if (_team == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("This team not exist at here..");

		}

		long count = teamRepository.count();

		cleaner.removeTeam(teamId);

		return count != teamRepository.count();
	}

	@Override
	public TeamResponseDTO findByAtheletesContainingIgnoreCase(String atheleteId) {

		if (atheleteId == null) {

			throw new NullPointerException("False request....");

		}

		Team team = teamRepository.findByAtheletesContainingIgnoreCase(atheleteId);

		return getTeamResponseFromTeam(team);
	}

	@Override
	public TeamResponseDTO findByCoachesContainingIgnoreCase(String coachId) {

		if (coachId == null) {

			throw new NullPointerException("False request...");

		}

		Team team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

		return getTeamResponseFromTeam(team);
	}

	@Override
	public TeamResponseDTO findByScoutsContainingIgnoreCase(String scoutId) {

		if (scoutId == null) {

			throw new NullPointerException("False request...");

		}

		Team team = teamRepository.findByScoutsContainingIgnoreCase(scoutId);

		return getTeamResponseFromTeam(team);
	}

	@Override
	public List<TeamResponseDTO> findByMatchesContainingIgnoreCase(String matchId) {

		if (matchId == null) {

			throw new NullPointerException("False request...");

		}

		List<Team> team = teamRepository.findByMatchesContainingIgnoreCase(matchId);

		return batchBuildTeamResponses(team);
	}

	@Override
	public List<TeamResponseDTO> findByTeamOwnerId(String teamOwnerId) {

		if (teamOwnerId == null) {

			throw new NullPointerException("False request...");

		}

		List<Team> list = teamRepository.findByTeamOwnerId(teamOwnerId);

		return batchBuildTeamResponses(list);
	}

	@Override
	public TeamResponseDTO findByDoctorsContainingIgnoreCase(String doctorId) {

		if (doctorId == null) {

			throw new NullPointerException("False request...");

		}

		Team team = teamRepository.findByDoctorsContainingIgnoreCase(doctorId);

		if (team == null) {

			throw new NoSuchElementException("No such team find at here...");

		}

		return getTeamResponseFromTeam(team);
	}

	@Override
	public TeamResponseDTO findByTeamName(String teamName) {

		if (teamName == null) {

			throw new NullPointerException("False request...");

		}

		Team team = teamRepository.findByTeamNameIgnoreCase(teamName);

		return getTeamResponseFromTeam(team);

	}

	@Override
	public TeamResponseDTO findByTeamId(String teamId) {

		if (teamId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			return getTeamResponseFromTeam(teamRepository.findById(teamId).get());

		} catch (Exception e) {

			throw new NoSuchElementException("No such scouts find at here..");

		}

	}

	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/**
	 * 🔥 Build single team response
	 */
	private TeamResponseDTO getTeamResponseFromTeam(Team team) {
		if (team == null)
			return null;
		List<TeamResponseDTO> responses = batchBuildTeamResponses(List.of(team));
		return responses.isEmpty() ? null : responses.get(0);
	}

	/**
	 * 🔥 OPTIMIZED: Batch build multiple team responses This method collects ALL
	 * data in BATCH queries
	 */
	private List<TeamResponseDTO> batchBuildTeamResponses(List<Team> teams) {
		if (teams == null || teams.isEmpty()) {
			return new ArrayList<>();
		}

		// 🔥 STEP 1: Collect all IDs from all teams
		List<String> allAthleteIds = teams.stream().filter(team -> team.getAtheletes() != null)
				.flatMap(team -> team.getAtheletes().stream()).distinct().collect(Collectors.toList());

		List<String> allScoutIds = teams.stream().filter(team -> team.getScouts() != null)
				.flatMap(team -> team.getScouts().stream()).distinct().collect(Collectors.toList());

		List<String> allCoachIds = teams.stream().filter(team -> team.getCoaches() != null)
				.flatMap(team -> team.getCoaches().stream()).distinct().collect(Collectors.toList());

		List<String> allDoctorIds = teams.stream().filter(team -> team.getDoctors() != null)
				.flatMap(team -> team.getDoctors().stream()).distinct().collect(Collectors.toList());

		List<String> allMatchIds = teams.stream().filter(team -> team.getMatches() != null)
				.flatMap(team -> team.getMatches().stream()).distinct().collect(Collectors.toList());

		List<String> allTeamOwnerIds = teams.stream().map(Team::getTeamOwnerId).filter(Objects::nonNull).distinct()
				.collect(Collectors.toList());

		CompletableFuture<Map<String, TeamOwner>> teamOwnerFuture = CompletableFuture
				.supplyAsync(() -> teamOwnerRepository.findAllById(allTeamOwnerIds).stream().distinct()
						.collect(Collectors.toMap(TeamOwner::getId, owner -> owner)), executor);

		CompletableFuture<Map<String, Athelete>> athleteFuture = CompletableFuture.supplyAsync(
				() -> !allAthleteIds.isEmpty() ? atheleteRepository.findAllById(allAthleteIds).stream().distinct()
						.collect(Collectors.toMap(Athelete::getId, athlete -> athlete)) : new HashMap<>(),
				executor);

		CompletableFuture<Map<String, Scouts>> scoutsFuture = CompletableFuture.supplyAsync(
				() -> !allScoutIds.isEmpty() ? scoutsRepository.findAllById(allScoutIds).stream().distinct()
						.collect(Collectors.toMap(Scouts::getId, scout -> scout)) : new HashMap<>(),
				executor);

		CompletableFuture<Map<String, Coach>> coachFuture = CompletableFuture
				.supplyAsync(() -> !allCoachIds.isEmpty() ? coachRepository.findAllById(allCoachIds).stream().distinct()
						.collect(Collectors.toMap(Coach::getId, coach -> coach)) : new HashMap<>(), executor);

		CompletableFuture<Map<String, Doctor>> doctorFuture = CompletableFuture
				.supplyAsync(
						() -> !allDoctorIds.isEmpty() ? doctorRepository.findAllById(allDoctorIds).stream()
								.collect(Collectors.toMap(Doctor::getId, doctor -> doctor)) : new HashMap<>(),
						executor);

		CompletableFuture<Map<String, String>> matchNameFuture = CompletableFuture.supplyAsync(() -> {
			try {
				List<MatchName> matchNamesList = matchNameRepository.findByMatchIdIn(allMatchIds);

				if (matchNamesList == null || matchNamesList.isEmpty()) {
					return Collections.emptyMap();
				}

				return matchNamesList.stream().filter(mn -> mn != null && mn.getMatchId() != null)
						.collect(Collectors.toMap(MatchName::getMatchId,
								mn -> mn.getName() != null && !mn.getName().isBlank() ? mn.getName() : "Unknown Match",
								(existing, replacement) -> existing // Keep first on duplicate
				));

			} catch (Exception e) {
				// log.error("Error fetching match names: {}", e.getMessage());
				return Collections.emptyMap();
			}
		}, executor);

		CompletableFuture
				.allOf(teamOwnerFuture, athleteFuture, scoutsFuture, coachFuture, doctorFuture, matchNameFuture).join();

		// 🔥 STEP 2: Batch fetch ALL data
		Map<String, TeamOwner> teamOwnerMap = teamOwnerFuture.join();

		Map<String, Athelete> athleteMap = athleteFuture.join();

		Map<String, Scouts> scoutMap = scoutsFuture.join();

		Map<String, Coach> coachMap = coachFuture.join();

		Map<String, Doctor> doctorMap = doctorFuture.join();

		// 🔥 STEP 3: Fetch match names (preserving order)
		Map<String, String> matchNameMap = matchNameFuture.join();

		// 🔥 STEP 4: Collect all User IDs
		List<String> allUserIds = new ArrayList<>();

		for (Athelete athlete : athleteMap.values()) {
			if (athlete.getUserId() != null)
				allUserIds.add(athlete.getUserId());
		}

		for (TeamOwner owner : teamOwnerMap.values()) {
			if (owner.getAtheleteId() != null) {
				Athelete athlete = athleteMap.get(owner.getAtheleteId());
				if (athlete != null && athlete.getUserId() != null) {
					allUserIds.add(athlete.getUserId());
				}
			}
		}

		for (Scouts scout : scoutMap.values()) {
			if (scout.getAtheleteId() != null) {
				Athelete athlete = athleteMap.get(scout.getAtheleteId());
				if (athlete != null && athlete.getUserId() != null) {
					allUserIds.add(athlete.getUserId());
				}
			}
		}

		for (Coach coach : coachMap.values()) {
			if (coach.getAtheleteId() != null) {
				Athelete athlete = athleteMap.get(coach.getAtheleteId());
				if (athlete != null && athlete.getUserId() != null) {
					allUserIds.add(athlete.getUserId());
				}
			}
		}

		for (Doctor doctor : doctorMap.values()) {
			if (doctor.getUserId() != null)
				allUserIds.add(doctor.getUserId());
		}

		allUserIds = allUserIds.stream().distinct().collect(Collectors.toList());

		// 🔥 STEP 5: Batch fetch all Users
		Map<String, User> userMap = !allUserIds.isEmpty()
				? userRepository.findAllById(allUserIds).stream().collect(Collectors.toMap(User::getId, user -> user))
				: new HashMap<>();

		// 🔥 STEP 6: Build responses
		List<TeamResponseDTO> responses = new ArrayList<>();

		for (Team team : teams) {
			TeamResponseDTO response = new TeamResponseDTO();

			// Basic info
			response.setId(team.getId());
			response.setAthletes(team.getAtheletes());
			response.setCoaches(team.getCoaches());
			response.setScouts(team.getScouts());
			response.setTeamName(team.getTeamName());
			response.setDoctors(team.getDoctors());
			response.setMatches(team.getMatches());
			response.setTeamOwnerId(team.getTeamOwnerId());

			// 🔥 Set Team Owner Name
			if (team.getTeamOwnerId() != null) {
				TeamOwner owner = teamOwnerMap.get(team.getTeamOwnerId());
				if (owner != null && owner.getAtheleteId() != null) {
					Athelete ownerAthlete = athleteMap.get(owner.getAtheleteId());
					if (ownerAthlete != null && ownerAthlete.getUserId() != null) {
						User ownerUser = userMap.get(ownerAthlete.getUserId());
						if (ownerUser != null) {
							response.setTeamOwnerName(ownerUser.getName());
						}
					}
				}
			}

			// 🔥 Set Athletes Names (preserving order)
			List<String> athletesName = new ArrayList<>();
			if (team.getAtheletes() != null) {
				for (String athleteId : team.getAtheletes()) {
					Athelete athlete = athleteMap.get(athleteId);
					if (athlete != null && athlete.getUserId() != null) {
						User user = userMap.get(athlete.getUserId());
						String name = (user != null && user.getName() != null && !user.getName().isBlank())
								? user.getName()
								: "Unnamed athlete";
						athletesName.add(name);
					} else {
						athletesName.add("Unknown athlete");
					}
				}
			}
			response.setAthletesName(athletesName);

			// 🔥 Set Scouts Names (preserving order)
			List<String> scoutsName = new ArrayList<>();
			if (team.getScouts() != null) {
				for (String scoutId : team.getScouts()) {
					Scouts scout = scoutMap.get(scoutId);
					if (scout != null && scout.getAtheleteId() != null) {
						Athelete scoutAthlete = athleteMap.get(scout.getAtheleteId());
						if (scoutAthlete != null && scoutAthlete.getUserId() != null) {
							User user = userMap.get(scoutAthlete.getUserId());
							String name = (user != null && user.getName() != null && !user.getName().isBlank())
									? user.getName()
									: "Unnamed scout";
							scoutsName.add(name);
						} else {
							scoutsName.add("Unknown scout athlete");
						}
					} else {
						scoutsName.add("Unknown scout");
					}
				}
			}
			response.setScoutsName(scoutsName);

			// 🔥 Set Coaches Names (preserving order)
			List<String> coachesName = new ArrayList<>();
			if (team.getCoaches() != null) {
				for (String coachId : team.getCoaches()) {
					Coach coach = coachMap.get(coachId);
					if (coach != null && coach.getAtheleteId() != null) {
						Athelete coachAthlete = athleteMap.get(coach.getAtheleteId());
						if (coachAthlete != null && coachAthlete.getUserId() != null) {
							User user = userMap.get(coachAthlete.getUserId());
							String name = (user != null && user.getName() != null && !user.getName().isBlank())
									? user.getName()
									: "Unnamed coach";
							coachesName.add(name);
						} else {
							coachesName.add("Unknown coach athlete");
						}
					} else {
						coachesName.add("Unknown coach");
					}
				}
			}
			response.setCoachesName(coachesName);

			// 🔥 Set Doctors Names (preserving order)
			List<String> doctorsName = new ArrayList<>();
			if (team.getDoctors() != null) {
				for (String doctorId : team.getDoctors()) {
					Doctor doctor = doctorMap.get(doctorId);
					if (doctor != null && doctor.getUserId() != null) {
						User user = userMap.get(doctor.getUserId());
						String name = (user != null && user.getName() != null && !user.getName().isBlank())
								? user.getName()
								: "Unnamed doctor";
						doctorsName.add(name);
					} else {
						doctorsName.add("Unknown doctor");
					}
				}
			}
			response.setDoctorsName(doctorsName);

			// 🔥 Set Matches Names (PRESERVING ORDER - CRITICAL FIX)
			List<String> matchesName = new ArrayList<>();

			int index = 0;

			if (team.getMatches() != null) {
				for (String matchId : team.getMatches()) {
					String matchName = matchNameMap.get(matchId);
					if (matchName != null && !matchName.isBlank()) {
						matchesName.add(matchName);
					} else {

						String myMatchName = "match-" + (team.getMatches().size() + index);

						matchesName.add(myMatchName);
					}

					++index;

				}
			}
			response.setMatchesName(matchesName);
			// ✅ Now matchesName is in EXACT same order as team.getMatches()
			// Index 0 in team.getMatches() corresponds to Index 0 in matchesName

			responses.add(response);
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

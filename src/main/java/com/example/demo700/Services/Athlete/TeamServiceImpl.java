package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
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
	    if (team == null) return null;
	    
	    List<TeamResponseDTO> responses = batchBuildTeamResponses(List.of(team));
	    return responses.isEmpty() ? null : responses.get(0);
	}

	/**
	 * 🚀 SUPER OPTIMIZED: Batch build multiple team responses
	 * - Single pass data collection
	 * - Parallel batch queries
	 * - Preserves order
	 * - No N+1 queries
	 */
	private List<TeamResponseDTO> batchBuildTeamResponses(List<Team> teams) {
	    if (teams == null || teams.isEmpty()) {
	        return new ArrayList<>();
	    }

	    long startTime = System.currentTimeMillis();
	    
	    // 🔥 STEP 1: Single pass - Collect all IDs
	    BatchIdCollector idCollector = collectAllIds(teams);
	    
	    // 🔥 STEP 2: Fetch all data in PARALLEL (7 parallel queries max)
	 // 🔥 STEP 2: Fetch all data in PARALLEL - Direct approach (Recommended)
	    CompletableFuture<Map<String, TeamOwner>> teamOwnerFuture = 
	        CompletableFuture.supplyAsync(() -> fetchTeamOwners(idCollector.teamOwnerIds), executor);

	    CompletableFuture<Map<String, Athelete>> athleteFuture = 
	        CompletableFuture.supplyAsync(() -> fetchAthletes(idCollector.athleteIds), executor);

	    CompletableFuture<Map<String, Scouts>> scoutsFuture = 
	        CompletableFuture.supplyAsync(() -> fetchScouts(idCollector.scoutIds), executor);

	    CompletableFuture<Map<String, Coach>> coachFuture = 
	        CompletableFuture.supplyAsync(() -> fetchCoaches(idCollector.coachIds), executor);

	    CompletableFuture<Map<String, Doctor>> doctorFuture = 
	        CompletableFuture.supplyAsync(() -> fetchDoctors(idCollector.doctorIds), executor);

	    CompletableFuture<Map<String, String>> matchNameFuture = 
	        CompletableFuture.supplyAsync(() -> fetchMatchNames(idCollector.matchIds), executor);
	    // Wait for all parallel queries to complete
	    CompletableFuture.allOf(teamOwnerFuture, athleteFuture, scoutsFuture, 
	                           coachFuture, doctorFuture, matchNameFuture).join();
	    
	    // Get results
	    Map<String, TeamOwner> teamOwnerMap = teamOwnerFuture.join();
	    Map<String, Athelete> athleteMap = athleteFuture.join();
	    Map<String, Scouts> scoutMap = scoutsFuture.join();
	    Map<String, Coach> coachMap = coachFuture.join();
	    Map<String, Doctor> doctorMap = doctorFuture.join();
	    Map<String, String> matchNameMap = matchNameFuture.join();
	    
	    Set<String> remainingAthlete = new HashSet<>();
	    
	    for(Scouts scout : scoutMap.values()) {
	    	
	    	remainingAthlete.add(scout.getAtheleteId());
	    	
	    }
	    
	    for(Coach coach : coachMap.values()) {
	    	
	    	remainingAthlete.add(coach.getAtheleteId());
	    	
	    }
	    
	    for(TeamOwner owner : teamOwnerMap.values()) {
	    	
	    	remainingAthlete.add(owner.getAtheleteId());
	    	
	    }
	    
	    idCollector.athleteIds.addAll(remainingAthlete);
	    
	    athleteMap = fetchAthletes(idCollector.athleteIds);
	    
	    System.out.println("scouts map :- " + scoutMap);
	    System.out.println("Team owner map :- " + teamOwnerMap);
	    
	    // 🔥 STEP 3: Batch fetch ALL users in ONE query (most critical optimization)
	    Map<String, User> userMap = fetchAllUsersBatch(teams, athleteMap, teamOwnerMap, 
	                                                    scoutMap, coachMap, doctorMap);
	    
	    System.out.println("User Map :- " + userMap);
	    
	    // 🔥 STEP 4: Build responses in O(n) time
	    List<TeamResponseDTO> responses = buildResponses(teams, teamOwnerMap, athleteMap, 
	                                                      scoutMap, coachMap, doctorMap, 
	                                                      matchNameMap, userMap);
	    
	    long endTime = System.currentTimeMillis();
	    System.out.println("Batch processed " + teams.size() + " teams in " + 
	                       (endTime - startTime) + "ms");
	    
	    return responses;
	}

	/**
	 * 🚀 Single pass ID collector to avoid multiple stream traversals
	 */
	private BatchIdCollector collectAllIds(List<Team> teams) {
	    BatchIdCollector collector = new BatchIdCollector();
	    
	    for (Team team : teams) {
	        if (team == null) continue;
	        
	        // Team owner
	        if (team.getTeamOwnerId() != null) {
	            collector.teamOwnerIds.add(team.getTeamOwnerId());
	        }
	        
	        // Athletes
	        if (team.getAtheletes() != null) {
	            collector.athleteIds.addAll(team.getAtheletes());
	        }
	        
	        // Scouts
	        if (team.getScouts() != null) {
	            collector.scoutIds.addAll(team.getScouts());
	        }
	        
	        // Coaches
	        if (team.getCoaches() != null) {
	            collector.coachIds.addAll(team.getCoaches());
	        }
	        
	        // Doctors
	        if (team.getDoctors() != null) {
	            collector.doctorIds.addAll(team.getDoctors());
	        }
	        
	        // Matches
	        if (team.getMatches() != null) {
	            collector.matchIds.addAll(team.getMatches());
	        }
	    }
	    
	    return collector;
	}

	/**
	 * 🔥 Batch fetch helper with parallel execution
	 */
	private <T> CompletableFuture<Map<String, T>> fetchBatchAsync(Supplier<Map<String, T>> supplier) {
	    return CompletableFuture.supplyAsync(supplier, executor);
	}

	/**
	 * 🚀 Fetch all users in ONE BATCH query (most important optimization)
	 */
	private Map<String, User> fetchAllUsersBatch(List<Team> teams, 
	                                              Map<String, Athelete> athleteMap,
	                                              Map<String, TeamOwner> teamOwnerMap,
	                                              Map<String, Scouts> scoutMap,
	                                              Map<String, Coach> coachMap,
	                                              Map<String, Doctor> doctorMap) {
	    Set<String> allUserIds = new HashSet<>();
	    
	    // Collect athlete userIds
	    for (Athelete athlete : athleteMap.values()) {
	        if (athlete != null && athlete.getUserId() != null) {
	            allUserIds.add(athlete.getUserId());
	        }
	    }
	    
	    // Collect team owner userIds (via athlete)
	    for (TeamOwner owner : teamOwnerMap.values()) {
	        if (owner != null && owner.getAtheleteId() != null) {
	            Athelete athlete = athleteMap.get(owner.getAtheleteId());
	            if (athlete != null && athlete.getUserId() != null) {
	                allUserIds.add(athlete.getUserId());
	            }
	        }
	    }
	    
	    // Collect scout userIds
	    for (Scouts scout : scoutMap.values()) {
	        if (scout != null && scout.getAtheleteId() != null) {
	            Athelete athlete = athleteMap.get(scout.getAtheleteId());
	            if (athlete != null && athlete.getUserId() != null) {
	                allUserIds.add(athlete.getUserId());
	            }
	        }
	    }
	    
	    // Collect coach userIds
	    for (Coach coach : coachMap.values()) {
	        if (coach != null && coach.getAtheleteId() != null) {
	            Athelete athlete = athleteMap.get(coach.getAtheleteId());
	            if (athlete != null && athlete.getUserId() != null) {
	                allUserIds.add(athlete.getUserId());
	            }
	        }
	    }
	    
	    // Collect doctor userIds directly
	    for (Doctor doctor : doctorMap.values()) {
	        if (doctor != null && doctor.getUserId() != null) {
	            allUserIds.add(doctor.getUserId());
	        }
	    }
	    
	    // ONE batch query for ALL users
	    if (allUserIds.isEmpty()) {
	        return Collections.emptyMap();
	    }
	    
	    return userRepository.findAllById(new ArrayList<>(allUserIds))
	        .stream()
	        .collect(Collectors.toMap(User::getId, Function.identity()));
	}

	/**
	 * 🔥 Fetch helper methods with null safety
	 */
	private Map<String, TeamOwner> fetchTeamOwners(Set<String> teamOwnerIds) {
	    if (teamOwnerIds == null || teamOwnerIds.isEmpty()) return Collections.emptyMap();
	    return teamOwnerRepository.findAllById(new ArrayList<>(teamOwnerIds))
	        .stream()
	        .collect(Collectors.toMap(TeamOwner::getId, Function.identity()));
	}

	private Map<String, Athelete> fetchAthletes(Set<String> athleteIds) {
	    if (athleteIds == null || athleteIds.isEmpty()) return Collections.emptyMap();
	    return atheleteRepository.findAllById(new ArrayList<>(athleteIds))
	        .stream()
	        .collect(Collectors.toMap(Athelete::getId, Function.identity()));
	}

	private Map<String, Scouts> fetchScouts(Set<String> scoutIds) {
		
		System.out.println("Checking null values....");
		
	    if (scoutIds == null || scoutIds.isEmpty()) return Collections.emptyMap();
	    
	    System.out.println("returning scout name map");
	    
	    return scoutsRepository.findAllById(scoutIds)
	        .stream()
	        .collect(Collectors.toMap(Scouts::getId, Function.identity()));
	}

	private Map<String, Coach> fetchCoaches(Set<String> coachIds) {
	    if (coachIds == null || coachIds.isEmpty()) return Collections.emptyMap();
	    return coachRepository.findAllById(new ArrayList<>(coachIds))
	        .stream()
	        .collect(Collectors.toMap(Coach::getId, Function.identity()));
	}

	private Map<String, Doctor> fetchDoctors(Set<String> doctorIds) {
	    if (doctorIds == null || doctorIds.isEmpty()) return Collections.emptyMap();
	    return doctorRepository.findAllById(new ArrayList<>(doctorIds))
	        .stream()
	        .collect(Collectors.toMap(Doctor::getId, Function.identity()));
	}

	private Map<String, String> fetchMatchNames(Set<String>matchIds) {
	    if (new ArrayList<>(matchIds) == null || new ArrayList<>(matchIds).isEmpty()) return Collections.emptyMap();
	    
	    return matchNameRepository.findByMatchIdIn(new ArrayList<>(matchIds))
	        .stream()
	        .filter(mn -> mn != null && mn.getMatchId() != null)
	        .collect(Collectors.toMap(
	            MatchName::getMatchId,
	            mn -> (mn.getName() != null && !mn.getName().isBlank()) ? mn.getName() : "Unknown Match",
	            (existing, replacement) -> existing
	        ));
	}

	/**
	 * 🚀 Build responses in O(n) time with order preservation
	 */
	private List<TeamResponseDTO> buildResponses(List<Team> teams,
	                                              Map<String, TeamOwner> teamOwnerMap,
	                                              Map<String, Athelete> athleteMap,
	                                              Map<String, Scouts> scoutMap,
	                                              Map<String, Coach> coachMap,
	                                              Map<String, Doctor> doctorMap,
	                                              Map<String, String> matchNameMap,
	                                              Map<String, User> userMap) {
	    
	    List<TeamResponseDTO> responses = new ArrayList<>(teams.size());
	    
	    for (Team team : teams) {
	        if (team == null) continue;
	        
	        TeamResponseDTO response = new TeamResponseDTO();
	        
	        // Set basic info
	        response.setId(team.getId());
	        response.setTeamName(team.getTeamName());
	        response.setTeamOwnerId(team.getTeamOwnerId());
	        response.setAthletes(team.getAtheletes());
	        response.setCoaches(team.getCoaches());
	        response.setScouts(team.getScouts());
	        response.setDoctors(team.getDoctors());
	        response.setMatches(team.getMatches());
	        
	        // Set Team Owner Name
	        response.setTeamOwnerName(getTeamOwnerName(team, teamOwnerMap, athleteMap, userMap));
	        
	        // Set names preserving order
	        response.setAthletesName(getNamesInOrder(team.getAtheletes(), athleteMap, userMap, "athlete"));
	        response.setCoachesName(getCoachNamesInOrder(team.getCoaches(), coachMap, athleteMap, userMap));
	        response.setScoutsName(getScoutNamesInOrder(team.getScouts(), scoutMap, athleteMap, userMap));
	        response.setDoctorsName(getDoctorNamesInOrder(team.getDoctors(), doctorMap, userMap));
	        response.setMatchesName(getMatchNamesInOrder(team.getMatches(), matchNameMap));
	        
	        responses.add(response);
	    }
	    
	    return responses;
	}

	/**
	 * Helper: Get team owner name
	 */
	private String getTeamOwnerName(Team team, Map<String, TeamOwner> teamOwnerMap,
	                                 Map<String, Athelete> athleteMap, Map<String, User> userMap) {
	    if (team.getTeamOwnerId() == null) return "Unknown";
	    
	    TeamOwner owner = teamOwnerMap.get(team.getTeamOwnerId());
	    if (owner == null || owner.getAtheleteId() == null) return "Unknown";
	    
	    Athelete athlete = athleteMap.get(owner.getAtheleteId());
	    if (athlete == null || athlete.getUserId() == null) return "Unknown";
	    
	    User user = userMap.get(athlete.getUserId());
	    return (user != null && user.getName() != null && !user.getName().isBlank()) 
	           ? user.getName() : "Unknown";
	}

	/**
	 * Helper: Get athlete names preserving order
	 */
	private List<String> getNamesInOrder(List<String> athleteIds, Map<String, Athelete> athleteMap,
	                                      Map<String, User> userMap, String defaultName) {
	    if (athleteIds == null || athleteIds.isEmpty()) return new ArrayList<>();
	    
	    List<String> names = new ArrayList<>(athleteIds.size());
	    for (String id : athleteIds) {
	        Athelete athlete = athleteMap.get(id);
	        if (athlete != null && athlete.getUserId() != null) {
	            User user = userMap.get(athlete.getUserId());
	            String name = (user != null && user.getName() != null && !user.getName().isBlank())
	                          ? user.getName() : defaultName;
	            names.add(name);
	        } else {
	            names.add("Unknown " + defaultName);
	        }
	    }
	    return names;
	}

	/**
	 * Helper: Get coach names preserving order
	 */
	private List<String> getCoachNamesInOrder(List<String> coachIds, Map<String, Coach> coachMap,
	                                           Map<String, Athelete> athleteMap, Map<String, User> userMap) {
	    if (coachIds == null || coachIds.isEmpty()) return new ArrayList<>();
	    
	    List<String> names = new ArrayList<>(coachIds.size());
	    for (String id : coachIds) {
	        Coach coach = coachMap.get(id);
	        if (coach != null && coach.getAtheleteId() != null) {
	            Athelete athlete = athleteMap.get(coach.getAtheleteId());
	            if (athlete != null && athlete.getUserId() != null) {
	                User user = userMap.get(athlete.getUserId());
	                String name = (user != null && user.getName() != null && !user.getName().isBlank())
	                              ? user.getName() : "coach";
	                names.add(name);
	            } else {
	                names.add("Unknown coach");
	            }
	        } else {
	            names.add("Unknown coach");
	        }
	    }
	    return names;
	}

	/**
	 * Helper: Get scout names preserving order
	 */
	private List<String> getScoutNamesInOrder(List<String> scoutIds, Map<String, Scouts> scoutMap,
	                                           Map<String, Athelete> athleteMap, Map<String, User> userMap) {
	    
		System.out.println("Checking nul values...");
		
		if (scoutIds == null || scoutIds.isEmpty()) return new ArrayList<>();
	    
		System.out.println("Null value passed");
		
	    List<String> names = new ArrayList<>(scoutIds.size());
	    for (String id : scoutIds) {
	        Scouts scout = scoutMap.get(id);
	        
	        System.out.println("scout in loop :- " + scout.toString());
	        
	        if (scout != null && scout.getAtheleteId() != null) {
	            Athelete athlete = athleteMap.get(scout.getAtheleteId());
	            
	            System.out.println("athlete in loop :- " + athlete);
	            
	            if (athlete != null && athlete.getUserId() != null) {
	                User user = userMap.get(athlete.getUserId());
	                
	                System.out.println("User map in loop :- " + user);
	                
	                String name = (user != null && user.getName() != null && !user.getName().isBlank())
	                              ? user.getName() : "scout";
	                names.add(name);
	            } else {
	            	
	            	System.out.println("Un known name from the athlete");
	            	
	                names.add("Unknown scout");
	            }
	        } else {
	        	
	        	System.out.println("Un known name from the scout...");
	        	
	            names.add("Unknown scout");
	        }
	    }
	    return names;
	}

	/**
	 * Helper: Get doctor names preserving order
	 */
	private List<String> getDoctorNamesInOrder(List<String> doctorIds, Map<String, Doctor> doctorMap,
	                                            Map<String, User> userMap) {
	    if (doctorIds == null || doctorIds.isEmpty()) return new ArrayList<>();
	    
	    List<String> names = new ArrayList<>(doctorIds.size());
	    for (String id : doctorIds) {
	        Doctor doctor = doctorMap.get(id);
	        if (doctor != null && doctor.getUserId() != null) {
	            User user = userMap.get(doctor.getUserId());
	            String name = (user != null && user.getName() != null && !user.getName().isBlank())
	                          ? user.getName() : "doctor";
	            names.add(name);
	        } else {
	            names.add("Unknown doctor");
	        }
	    }
	    return names;
	}

	/**
	 * Helper: Get match names preserving order
	 */
	private List<String> getMatchNamesInOrder(List<String> matchIds, Map<String, String> matchNameMap) {
	    if (matchIds == null || matchIds.isEmpty()) return new ArrayList<>();
	    
	    List<String> names = new ArrayList<>(matchIds.size());
	    for (int i = 0; i < matchIds.size(); i++) {
	        String matchId = matchIds.get(i);
	        String matchName = matchNameMap.get(matchId);
	        if (matchName != null && !matchName.isBlank()) {
	            names.add(matchName);
	        } else {
	            names.add("Match " + (i + 1));
	        }
	    }
	    return names;
	}

	/**
	 * Inner class for batch ID collection
	 */
	private static class BatchIdCollector {
	    Set<String> teamOwnerIds = new HashSet<>();
	    Set<String> athleteIds = new HashSet<>();
	    Set<String> scoutIds = new HashSet<>();
	    Set<String> coachIds = new HashSet<>();
	    Set<String> doctorIds = new HashSet<>();
	    Set<String> matchIds = new HashSet<>();
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

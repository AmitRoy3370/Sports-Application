package com.example.demo700.Services.Athlete;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.DoctorRepositories.DoctorRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;

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
			
			for(String doctorId : team.getDoctors()) {
				
				Doctor doctor = doctorRepository.findById(doctorId).get();
				
				if(doctor == null) {
					
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
	public List<Team> seeAllTeam() {

		return teamRepository.findAll();

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
	public Team findByAtheletesContainingIgnoreCase(String atheleteId) {

		if (atheleteId == null) {

			throw new NullPointerException("False request....");

		}

		Team team = teamRepository.findByAtheletesContainingIgnoreCase(atheleteId);

		return team;
	}

	@Override
	public Team findByCoachesContainingIgnoreCase(String coachId) {

		if (coachId == null) {

			throw new NullPointerException("False request...");

		}

		Team team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

		return team;
	}

	@Override
	public Team findByScoutsContainingIgnoreCase(String scoutId) {

		if (scoutId == null) {

			throw new NullPointerException("False request...");

		}

		Team team = teamRepository.findByScoutsContainingIgnoreCase(scoutId);

		return team;
	}

	@Override
	public List<Team> findByMatchesContainingIgnoreCase(String matchId) {

		if (matchId == null) {

			throw new NullPointerException("False request...");

		}

		List<Team> team = teamRepository.findByMatchesContainingIgnoreCase(matchId);

		return team;
	}

	@Override
	public List<Team> findByTeamOwnerId(String teamOwnerId) {

		if (teamOwnerId == null) {

			throw new NullPointerException("False request...");

		}

		List<Team> list = teamRepository.findByTeamOwnerId(teamOwnerId);

		return list;
	}

	@Override
	public Team findByDoctorsContainingIgnoreCase(String doctorId) {
		
		if(doctorId == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		Team team = teamRepository.findByDoctorsContainingIgnoreCase(doctorId);
		
		if(team == null) {
			
			throw new NoSuchElementException("No such team find at here...");
			
		}
		
		return team;
	}

}

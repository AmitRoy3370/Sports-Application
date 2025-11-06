package com.example.demo700.Services.Athlete;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Validator.URLValidator;

@Service
public class AtheleteServiceImpl implements AtheleteService {

	@Autowired
	AtheleteRepository autheleteRepository;

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

			Athelete _athelete = autheleteRepository.findByUserId(athelete.getUserId()).get();

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

		athelete = autheleteRepository.save(athelete);

		if (athelete == null) {

			return null;

		}

		return athelete;
	}

	@Override
	public List<Athelete> seeAll() {

		List<Athelete> list = autheleteRepository.findAll();

		if (list == null) {

			return null;

		}

		return list;
	}

	@Override
	public Athelete updateAthelete(Athelete athelete, String userId, String atheleteId) {

		if (athelete == null || userId == null || atheleteId == null) {

			throw new NullPointerException("Have to take input of all data...");

		}

		Athelete _athelete = autheleteRepository.findById(atheleteId).get();

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

		Athelete __athlete = autheleteRepository.findByUserId(user.getId()).get();
		
		if (!user.getRoles().contains(Role.ROLE_ATHLETE) || !__athlete.getId().equals(atheleteId)) {

			throw new ArithmeticException("False user request...");

		}
		
		try {
			
			if(__athlete.getPresentTeam() != null) {
				
				Team team1 = teamRepository.findByTeamName(__athlete.getPresentTeam());
				
				if(team1 == null) {
					
					throw new Exception();
					
				}
			
				if(!team1.getAtheletes().contains(atheleteId)) {
					
					throw new Exception();
					
				}
				
			}
			
		} catch(Exception e) {
			
			throw new ArithmeticException("Team information is not valid...");
			
		}

		try {

			Athelete __athelete = autheleteRepository.findByUserId(athelete.getId()).get();

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

		athelete.setId(_athelete.getId());

		athelete = autheleteRepository.save(athelete);

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

			Athelete athelete = autheleteRepository.findById(atheleteId).get();

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
				|| autheleteRepository.findByUserId(userId).equals(atheleteId)) {

			long count = autheleteRepository.count();

			cleaner.removeAthelete(atheleteId);

			boolean yes = count != autheleteRepository.count();

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
	public List<Athelete> searchAtheleteByTeamName(String teamName) {

		if (teamName == null) {

			throw new NullPointerException("No athelete find at here...");

		}

		List<Athelete> list = autheleteRepository.findBypresentTeam(teamName);

		return list;
	}

	@Override
	public List<Athelete> findByAgeLessThan(int age) {

		List<Athelete> list = autheleteRepository.findByAgeLessThan(age);

		return list;
	}

	@Override
	public List<Athelete> findByHeightGreaterThan(double height) {

		List<Athelete> list = autheleteRepository.findByHeightGreaterThan(height);

		return list;
	}

	@Override
	public List<Athelete> findByWeightLessThan(double weight) {
		List<Athelete> list = autheleteRepository.findByWeightLessThan(weight);

		return list;
	}

	@Override
	public List<Athelete> findByPresentTeamIgnoreCase(String teamName) {
		if (teamName == null) {

			throw new NullPointerException("No athelete find at here...");

		}

		List<Athelete> list = autheleteRepository.findByPresentTeamIgnoreCase(teamName);

		return list;
	}

	@Override
	public List<Athelete> findByPosition(int position) {
		List<Athelete> list = autheleteRepository.findByPosition(position);

		return list;
	}

	@Override
	public List<Athelete> findByEventAttendenceContainingIgnoreCase(String eventName) {

		if (eventName == null) {

			throw new NullPointerException("in valid request...");

		}

		List<Athelete> list = autheleteRepository.findByEventAttendenceContainingIgnoreCase(eventName);

		return list;
	}

	@Override
	public List<Athelete> findByGameLogsContainingIgnoreCase(String gameLog) {

		if (gameLog == null) {

			throw new NullPointerException("in valid request...");

		}

		List<Athelete> list = autheleteRepository.findByGameLogsContainingIgnoreCase(gameLog);

		return list;
	}

	@Override
	public List<Athelete> findByAgeLessThanAndHeightGreaterThan(int age, double height) {

		List<Athelete> list = autheleteRepository.findByAgeLessThanAndHeightGreaterThan(age, height);

		return list;

	}

	@Override
	public List<Athelete> searchByTeamNamePartial(String partialName) {
		if (partialName == null) {

			throw new NullPointerException("in valid request...");

		}

		List<Athelete> list = autheleteRepository.searchByTeamNamePartial(partialName);

		return list;
	}

	@Override
	public List<Athelete> findByMultipleEvents(List<String> eventNames) {

		if (eventNames == null) {

			throw new NullPointerException("False request...");

		}

		List<Athelete> list = autheleteRepository.findByMultipleEvents(eventNames);

		return list;

	}

	@Override
	public List<Athelete> findByWeightRange(double min, double max) {

		List<Athelete> list = autheleteRepository.findByWeightRange(min, max);

		return list;

	}

	@Override
	public Optional<Athelete> findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("false request...");

		}

		return autheleteRepository.findByUserId(userId);
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

		Athelete athelete = autheleteRepository.findById(userId).get();

		if (athelete == null) {

			return false;

		}

		if (!user.getRoles().contains(Role.ROLE_ADMIN) && !athelete.getUserId().equals(actionUserId)) {

			throw new ArithmeticException("Only center admin can remove atheletes...");

		}

		long count = autheleteRepository.count();

		cleaner.removeAthelete(athelete.getId());

		boolean yes = count != autheleteRepository.count();

		return yes;
	}

}

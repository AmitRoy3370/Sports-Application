package com.example.demo700.Services.Athlete;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;

@Service
public class ScoutServiceImpl implements ScoutService {

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MatchRepository matchRepository;
	
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
	public List<Scouts> seeAllScouts() {

		return scoutsRepository.findAll();

	}

	@Override
	public Scouts findByAtheleteId(String atheleteId) {

		if (atheleteId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Scouts scout = scoutsRepository.findByAtheleteId(atheleteId);

			if (scout == null) {

				throw new NoSuchElementException("Failed to find such scout....");

			}

			return scout;

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
	public List<Scouts> findByEventsContainingIgnoreCase(String eventId) {

		if (eventId == null) {

			throw new NullPointerException("False request...");

		}

		List<Scouts> list = scoutsRepository.findByEventsContainingIgnoreCase(eventId);

		if (list == null) {

			throw new NoSuchElementException("No scouts will participate such event...");

		}

		return list;
	}

	@Override
	public List<Scouts> findByMatchesContainingIgnoreCase(String eventId) {

		if (eventId == null) {

			throw new NullPointerException("False request...");

		}

		List<Scouts> list = scoutsRepository.findByMatchesContainingIgnoreCase(eventId);

		if (list == null) {

			throw new NoSuchElementException("No scouts will participate such event...");

		}

		return list;
	}

}

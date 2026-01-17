package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Validator.URLValidator;

@Service
public class CoachServiceImpl implements CoachService {

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	private URLValidator urlValidator = new URLValidator();

	private AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public Coach addCoach(Coach coach, String userId) {

		if (coach == null || userId == null) {

			throw new NullPointerException("have to give all the input properly...");

		}

		try {

			Athelete athelete = atheleteRepository.findById(coach.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("The given athele is not present....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (!user.getRoles().contains(Role.ROLE_ADMIN) && !user.getRoles().contains(Role.ROLE_COACH)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Your given actioned user is not valid...");

		}

		try {

			Coach list = coachRepository.findByAtheleteId(coach.getAtheleteId());

			if (list != null) {

				return null;

			}

		} catch (Exception e) {

		}

		if (coach.getAtheletesVideo() != null) {

			if (!urlValidator.isValid(coach.getAtheletesVideo())) {

				throw new ArithmeticException("Your given athlete's video url's are not valid...");

			}

		}

		if (coach.getPerformanceTracking() != null) {

			if (!urlValidator.isValid(coach.getPerformanceTracking())) {

				throw new ArithmeticException("Your performance tracking url's are not valid...");

			}

		}

		if (coach.getTeamName() != null) {

			throw new ArithmeticException("No coach can join in any team in time of creation...");

		}

		coach = coachRepository.save(coach);

		if (coach == null) {

			return null;

		}

		return coach;

	}

	@Override
	public List<Coach> seeAll() {

		List<Coach> list = coachRepository.findAll();

		return list;
	}

	@Override
	public Coach updateCoach(Coach coach, String userId, String coachId) {
		if (coach == null || userId == null) {

			throw new NullPointerException("have to give all the input properly...");

		}

		try {

			Athelete athelete = atheleteRepository.findById(coach.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("The given athele is not present....");

		}

		try {

			User user = userRepository.findById(userId).get();

			System.out.println("User find...");

			System.out.println(user.getId());

			Athelete athelete = atheleteRepository.findByUserId(user.getId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			System.out.println("Athlete find...");

			Coach _coach = coachRepository.findByAtheleteId(athelete.getId());

			if (_coach == null) {

				throw new Exception();

			}

			System.out.println("Coach find by athlete...");

			if (_coach.getId().equals(coachId) && !user.getRoles().contains(Role.ROLE_COACH)) {

				throw new Exception();

			}

			System.out.println("Everything is ok...");

		} catch (Exception e) {

			throw new ArithmeticException("Your given actioned user is not valid...");

		}

		try {

			Coach list = coachRepository.findByAtheleteId(coach.getAtheleteId());

			if (list == null || !(coachRepository.findById(coachId).get() != null
					&& coachRepository.findById(coachId).get().getAtheleteId().equals(list.getAtheleteId()))) {

				return null;

			}

		} catch (Exception e) {

		}

		try {

			Coach _coach = coachRepository.findById(coachId).get();

			if (_coach == null) {

				return null;

			}

		} catch (Exception e) {

			throw new ArithmeticException("No Coach is at here...");

		}

		if (coach.getAtheletesVideo() != null) {

			if (!urlValidator.isValid(coach.getAtheletesVideo())) {

				throw new ArithmeticException("Your given athlete's video url's are not valid...");

			}

		}

		if (coach.getPerformanceTracking() != null) {

			if (!urlValidator.isValid(coach.getPerformanceTracking())) {

				throw new ArithmeticException("Your performance tracking url's are not valid...");

			}

		}

		if (coach.getTeamName() != null) {

			try {

				Team team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

				if (!team.getCoaches().contains(coachId)) {

					throw new Exception();

				}

				if (!team.getTeamName().equalsIgnoreCase(coach.getTeamName())) {

					throw new Exception();

				}

			} catch (Exception e) {

				throw new ArithmeticException("In valid team information...");

			}

		}

		coach.setId(coachId);

		coach = coachRepository.save(coach);

		if (coach == null) {

			return null;

		}

		return coach;
	}

	@Override
	public boolean deleteCoach(String coachId, String userId) {

		try {

			User user = userRepository.findById(userId).get();

			Athelete athelete = atheleteRepository.findByUserId(user.getId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			Coach _coach = coachRepository.findByAtheleteId(athelete.getId());

			if (_coach == null) {

				throw new Exception();

			}

			if (!_coach.getId().equals(coachId) && !user.getRoles().contains(Role.ROLE_ADMIN)) {

				throw new Exception();

			}

			long count = coachRepository.count();

			cleaner.removeCoach(coachId);

			return count != coachRepository.count();

		} catch (Exception e) {

			throw new ArithmeticException("Your given actioned user is not valid...");

		}

	}

	@Override
	public Coach searchCoach(String coachId) {

		if (coachId == null) {

			return null;

		}

		Coach coach = null;

		try {

			coach = coachRepository.findById(coachId).get();

		} catch (Exception e) {

			return null;

		}

		return coach;

	}

	@Override
	public Coach findByAthleteId(String athleteId) {

		if (athleteId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Coach coach = coachRepository.findByAtheleteId(athleteId);

			if (coach == null) {

				throw new Exception();

			}

			return coach;

		} catch (Exception e) {

			throw new NoSuchElementException("No such coach find at here...");

		}

	}

	@Override
	public Coach findByCoachId(String coachId) {

		if (coachId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			return coachRepository.findById(coachId).get();

		} catch (Exception e) {

			throw new NoSuchElementException("No such coach find at here...");

		}

	}

	@Override
	public List<Coach> findByCoachClassification(AthleteClassificationTypes athleteClassificationTypes) {

		if (athleteClassificationTypes == null) {

			throw new NullPointerException("False request....");

		}

		List<Coach> list = new ArrayList<>();

		try {

			List<AthleteClassification> athletes = athleteClassificationRepository
					.findByAthleteClassificationTypes(athleteClassificationTypes);

			if (athletes.isEmpty()) {

				throw new Exception();

			}

			for (AthleteClassification i : athletes) {

				try {

					Athelete athlete = atheleteRepository.findById(i.getAthleteId()).get();

					Coach coach = coachRepository.findByAtheleteId(athlete.getId());

					if (coach != null) {

						list.add(coach);

					}

				} catch (Exception e) {

				}

			}

			if (list.isEmpty()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such coach find at here....");

		}

		return list;
	}

}

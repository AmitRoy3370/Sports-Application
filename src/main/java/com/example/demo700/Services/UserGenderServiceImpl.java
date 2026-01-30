package com.example.demo700.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Gender;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Models.Turf.Owner;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.DoctorRepositories.DoctorRepository;
import com.example.demo700.Repositories.Turf.OwnerRepository;

@Service
public class UserGenderServiceImpl implements UserGenderService {

	@Autowired
	private UserGenderRepository userGenderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public UserGender addUserGender(UserGender userGender) {

		if (userGender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			if (userGender.getGender() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such selected gender at here...");

		}

		try {

			User user = userRepository.findById(userGender.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserGender gender = userGenderRepository.findByUserId(userGender.getUserId());

			if (gender != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Your gender is setted already...");

		} catch (Exception e) {

		}

		userGender = userGenderRepository.save(userGender);

		if (userGender == null) {

			throw new ArithmeticException("User gender is not setted at here....");

		}

		return userGender;
	}

	@Override
	public UserGender updateUserGender(UserGender userGender, String userId, String id) {

		if (userGender == null || userId == null || id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			UserGender gender = userGenderRepository.findById(id).get();

			if (gender == null) {

				throw new Exception();

			}

			if (!gender.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user gender find at here...");

		}

		try {

			if (userGender.getGender() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such selected gender at here...");

		}

		try {

			User user = userRepository.findById(userGender.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserGender gender = userGenderRepository.findByUserId(userGender.getUserId());

			if (gender != null) {

				if (gender.getId().equals(userId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Your gender is setted already...");

		} catch (Exception e) {

		}

		userGender.setId(id);

		userGender = userGenderRepository.save(userGender);

		if (userGender == null) {

			throw new ArithmeticException("User gender is not setted at here....");

		}

		return userGender;
	}

	@Override
	public UserGender findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			UserGender gender = userGenderRepository.findById(id).get();

			if (gender == null) {

				throw new Exception();

			}

			return gender;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user gender find at here...");

		}

	}

	@Override
	public List<UserGender> seeAll() {

		try {

			List<UserGender> list = userGenderRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user gender find at here...");

		}

	}

	@Override
	public UserGender findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserGender gender = userGenderRepository.findByUserId(userId);

			if (gender == null) {

				throw new Exception();

			}

			return gender;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gender set till now....");

		}

	}

	@Override
	public List<UserGender> findByGender(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<UserGender> list = userGenderRepository.findByGender(gender);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gender find at here...");

		}

	}

	@Override
	public boolean deleteUserGender(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			UserGender gender = userGenderRepository.findById(id).get();

			if (gender == null) {

				throw new Exception();

			}

			User user = userRepository.findById(gender.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = userGenderRepository.count();

				cleaner.removeUserGender(id);

				return count != userGenderRepository.count();

			}

			if (!gender.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user gender find at here...");

		}

		long count = userGenderRepository.count();

		cleaner.removeUserGender(id);

		return count != userGenderRepository.count();
	}

	@Override
	public List<Athelete> findAllAthlete(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		List<Athelete> responseAthlete = new ArrayList<>();

		List<Athelete> list = athleteRepository.findAll();

		for (Athelete i : list) {

			try {

				UserGender userGender = userGenderRepository.findByUserId(i.getUserId());

				if (userGender.getGender() == gender) {

					responseAthlete.add(i);

				}

			} catch (Exception e) {

			}

		}

		if (responseAthlete.isEmpty()) {

			throw new NoSuchElementException("No such athlete find at here...");

		}

		return responseAthlete;
	}

	@Override
	public List<Coach> findAllCoach(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Coach> responseCoach = new ArrayList<>();

			List<Coach> list = coachRepository.findAll();

			for (Coach i : list) {

				try {

					Athelete athlete = athleteRepository.findById(i.getAtheleteId()).get();

					UserGender userGender = userGenderRepository.findByUserId(athlete.getUserId());

					if (userGender.getGender() == gender) {

						responseCoach.add(i);

					}

				} catch (Exception e) {

				}

			}

			if (responseCoach.isEmpty()) {

				throw new Exception();

			}

			return responseCoach;

		} catch (Exception e) {

			throw new NoSuchElementException("No such coach find at here...");

		}

	}

	@Override
	public List<Scouts> findAllScouts(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Scouts> responseScouts = new ArrayList<>();

			List<Scouts> list = scoutsRepository.findAll();

			for (Scouts i : list) {

				Athelete athlete = athleteRepository.findById(i.getAtheleteId()).get();

				UserGender userGender = userGenderRepository.findByUserId(athlete.getUserId());

				if (userGender.getGender() == gender) {

					responseScouts.add(i);

				}

			}

			if (responseScouts.isEmpty()) {

				throw new Exception();

			}

			return responseScouts;

		} catch (Exception e) {

			throw new NoSuchElementException("No such scouts find at here...");

		}

	}

	@Override
	public List<Owner> findAllVenueOwner(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Owner> responseOwners = new ArrayList<>();

			List<Owner> list = ownerRepository.findAll();

			for (Owner i : list) {

				try {

					UserGender userGender = userGenderRepository.findByUserId(i.getUserId());

					if (userGender.getGender() == gender) {

						responseOwners.add(i);

					}

				} catch (Exception e) {

				}

			}

			if (responseOwners.isEmpty()) {

				throw new Exception();

			}

			return responseOwners;

		} catch (Exception e) {

			throw new NoSuchElementException("No such owner find at here...");

		}

	}

	@Override
	public List<User> findAllGymTrainer(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<User> responseGymTrainer = new ArrayList<>();

			List<User> list = userRepository.findByRoles(Role.ROLE_GYM_TRAINER);

			for (User i : list) {

				UserGender userGender = userGenderRepository.findByUserId(i.getId());

				if (userGender.getGender() == gender) {

					responseGymTrainer.add(i);

				}

			}

			if (responseGymTrainer.isEmpty()) {

				throw new Exception();

			}

			return responseGymTrainer;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym triner find at here...");

		}

	}

	@Override
	public List<User> findAllGymOwner(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<User> responseGymTrainer = new ArrayList<>();

			List<User> list = userRepository.findByRoles(Role.ROLE_GYM_OWNER);

			for (User i : list) {

				UserGender userGender = userGenderRepository.findByUserId(i.getId());

				if (userGender.getGender() == gender) {

					responseGymTrainer.add(i);

				}

			}

			if (responseGymTrainer.isEmpty()) {

				throw new Exception();

			}

			return responseGymTrainer;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym owner find at here...");

		}
	}

	@Override
	public List<TeamOwner> findAllTeamOwner(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<TeamOwner> responseTeamOwner = new ArrayList<>();

			List<TeamOwner> list = teamOwnerRepository.findAll();

			for (TeamOwner i : list) {

				try {

					Athelete athlete = athleteRepository.findById(i.getAtheleteId()).get();

					UserGender userGender = userGenderRepository.findByUserId(athlete.getUserId());

					if (userGender.getGender() == gender) {

						responseTeamOwner.add(i);

					}

				} catch (Exception e) {

				}

			}

			if (responseTeamOwner.isEmpty()) {

				throw new Exception();

			}

			return responseTeamOwner;

		} catch (Exception e) {

			throw new NoSuchElementException("No such team owner find at here...");

		}

	}

	@Override
	public List<Doctor> findAllDoctor(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> responseDoctor = new ArrayList<>();

			List<Doctor> list = doctorRepository.findAll();

			for (Doctor i : list) {

				try {

					UserGender userGender = userGenderRepository.findByUserId(i.getUserId());

					if (userGender.getGender() == gender) {

						responseDoctor.add(i);

					}

				} catch (Exception e) {

				}

			}

			if (responseDoctor.isEmpty()) {

				throw new Exception();

			}

			return responseDoctor;

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here...");

		}

	}

}

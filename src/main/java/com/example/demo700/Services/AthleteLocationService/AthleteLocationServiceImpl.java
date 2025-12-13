package com.example.demo700.Services.AthleteLocationService;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;

@Service
public class AthleteLocationServiceImpl implements AthleteLocationService {

	@Autowired
	private AthleteLocationRepository athleteLocationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public AthleteLocation addAthleteLocation(AthleteLocation athleteLocation, String userId) {

		if (athleteLocation == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		if (athleteLocation.getAthleteId() == null || athleteLocation.getLocationName() == null
				|| athleteLocation.getLattitude() <= 0.0 || athleteLocation.getLongitude() <= 0.0) {

			throw new NullPointerException("Have to fill all the required data at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here..");

		}

		try {

			Athelete athlete = athleteRepository.findById(athleteLocation.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Invalid Athlete credential...");

		}

		athleteLocation = athleteLocationRepository.save(athleteLocation);

		if (athleteLocation == null) {

			throw new ArithmeticException("Athlete location is not added...");

		}

		return athleteLocation;
	}

	@Override
	public List<AthleteLocation> seeAllAthleteLocation() {

		List<AthleteLocation> list = athleteLocationRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such location find at here...");

		}

		return list;
	}

	@Override
	public AthleteLocation findByAthleteId(String athleteId) {

		if (athleteId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			AthleteLocation athleteLocation = athleteLocationRepository.findByAthleteId(athleteId);

			if (athleteLocation == null) {

				throw new Exception();

			}

			return athleteLocation;

		} catch (Exception e) {

			throw new NoSuchElementException("No Such athlete location exist at here...");

		}

	}

	@Override
	public List<AthleteLocation> findByLocationName(String locationName) {

		if (locationName == null) {

			throw new NullPointerException("False request...");

		}

		List<AthleteLocation> list = athleteLocationRepository.findByLocationNameIgnoreCase(locationName);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such location find at here...");

		}

		return list;
	}

	@Override
	public List<AthleteLocation> findByLattitudeAndLongitude(double lattitude, double longitude) {

		List<AthleteLocation> list = athleteLocationRepository.findByLattitudeAndLongitude(lattitude, longitude);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such location find at here...");

		}

		return list;
	}

	@Override
	public AthleteLocation updateAthleteLocation(AthleteLocation athleteLocation, String userId, String athleteId) {

		if (athleteLocation == null || userId == null || athleteId == null) {

			throw new NullPointerException("False request...");

		}

		if (athleteLocation.getAthleteId() == null || athleteLocation.getLocationName() == null
				|| athleteLocation.getLattitude() <= 0.0 || athleteLocation.getLongitude() <= 0.0) {

			throw new NullPointerException("Have to fill all the required data at here...");

		}

		try {

			AthleteLocation _athleteLocation = athleteLocationRepository.findById(athleteId).get();

			if (_athleteLocation == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such athlete location find at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here..");

		}

		try {

			Athelete athlete = athleteRepository.findById(athleteLocation.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Invalid Athlete credential...");

		}

		try {

			AthleteLocation _athleteLocation = athleteLocationRepository.findById(athleteId).get();

			if (_athleteLocation == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athleteLocation.getAthleteId().equals(athlete.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such athlete location find at hete...");

		}

		athleteLocation.setId(athleteId);

		athleteLocation = athleteLocationRepository.save(athleteLocation);

		if (athleteLocation == null) {

			throw new ArithmeticException("Athlete location is not added...");

		}

		return athleteLocation;
	}

	@Override
	public boolean deleteAthleteLocation(String userId, String athleteId) {

		if (userId == null || athleteId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			AthleteLocation athleteLocation = athleteLocationRepository.findById(athleteId).get();

			if (athleteLocation == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such athlete location find at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = athleteLocationRepository.count();

				cleaner.removeAthleteLocation(athleteId);

				return count != athleteRepository.count();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here..");

		}

		try {

			AthleteLocation athleteLocation = athleteLocationRepository.findById(athleteId).get();

			if (athleteLocation == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(athleteLocation.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Invalid Athlete credential...");

		}

		long count = athleteLocationRepository.count();

		cleaner.removeAthleteLocation(athleteId);

		return count != athleteRepository.count();
	}

}

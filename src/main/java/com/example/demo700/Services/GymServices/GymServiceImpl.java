package com.example.demo700.Services.GymServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.GymModels.Gyms;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.GymRepositories.GymsRepository;

@Service
public class GymServiceImpl implements GymService {

	@Autowired
	private GymsRepository gymsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public Gyms addGyms(Gyms gyms) {

		if (gyms == null) {

			throw new NullPointerException("False request...");

		}

		if (gyms.getMonthlyFee() <= 0.0 || gyms.getEntryFee() <= 0.0) {

			throw new ArithmeticException("Price is in valid...");

		}

		if (gyms.getLongtitude() <= 0.0 || gyms.getLatitude() <= 0.0) {

			throw new ArithmeticException("Location axis is in valid....");

		}

		if (gyms.getLocationName() == null) {

			throw new NullPointerException("Gyms must have a location name....");

		}

		try {

			User user = userRepository.findById(gyms.getGymTrainer()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_GYM_TRAINER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here.....");

		}

		try {

			if (gyms.getGymName() == null) {

				throw new Exception();

			}

			Gyms _gyms = gymsRepository.findByGymNameIgnoreCase(gyms.getGymName().trim());

			if (_gyms != null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("This gym name already exist at here....");

		}

		gyms = gymsRepository.save(gyms);

		if (gyms == null) {

			throw new ArithmeticException("value is not added....");

		}

		return gyms;
	}

	@Override
	public Gyms updateGyms(Gyms gyms, String userId, String gymId) {

		if (gyms == null || userId == null || gymId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms _gyms = gymsRepository.findById(gymId).get();

			if (_gyms == null) {

				throw new Exception();

			}

			if (!_gyms.getGymTrainer().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

		if (gyms.getMonthlyFee() <= 0.0 || gyms.getEntryFee() <= 0.0) {

			throw new ArithmeticException("Price is in valid...");

		}

		if (gyms.getLongtitude() <= 0.0 || gyms.getLatitude() <= 0.0) {

			throw new ArithmeticException("Location axis is in valid....");

		}

		if (gyms.getLocationName() == null) {

			throw new NullPointerException("Gyms must have a location name....");

		}

		try {

			User user = userRepository.findById(gyms.getGymTrainer()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_GYM_TRAINER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here.....");

		}

		try {

			if (gyms.getGymName() == null) {

				throw new Exception();

			}

			Gyms _gyms = gymsRepository.findByGymNameIgnoreCase(gyms.getGymName().trim());

			if (_gyms != null) {

				if (!_gyms.getId().equals(gymId)) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("This gym name already exist at here....");

		}

		gyms.setId(gymId);

		gyms = gymsRepository.save(gyms);

		if (gyms == null) {

			throw new ArithmeticException("value is not added....");

		}

		return gyms;
	}

	@Override
	public List<Gyms> findByGymTrainer(String gymTrainer) {

		if (gymTrainer == null) {

			throw new NullPointerException("False request....");

		}

		List<Gyms> list = gymsRepository.findByGymTrainer(gymTrainer);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public Gyms findByGymName(String gymName) {

		if (gymName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms gyms = gymsRepository.findByGymNameIgnoreCase(gymName.trim());

			if (gyms == null) {

				throw new Exception();

			}

			return gyms;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

	}

	@Override
	public List<Gyms> findByLocationName(String locationName) {

		if (locationName == null) {

			throw new NullPointerException("False request...");

		}

		List<Gyms> list = gymsRepository.findByLocationNameIgnoreCase(locationName.trim());

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> findByLatitudeAndLongtitude(double latitude, double longtitude) {

		List<Gyms> list = gymsRepository.findByLatitudeAndLongtitude(latitude, longtitude);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> findByEntryFeeLessThanOrEqual(double entryFee) {

		List<Gyms> list = gymsRepository.findByEntryFeeLessThanEqual(entryFee);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> findByMonthlyFeeLessThanOrEqual(double monthlyFee) {

		List<Gyms> list = gymsRepository.findByMonthlyFeeLessThanEqual(monthlyFee);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> seeAllGyms() {

		List<Gyms> list = gymsRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public Gyms findById(String gymId) {

		if (gymId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms gyms = gymsRepository.findById(gymId).get();

			if (gyms == null) {

				throw new Exception();

			}

			return gyms;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public boolean deleteGyms(String gymId, String userId) {

		if (gymId == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_GYM_TRAINER)) {

				long count = gymsRepository.count();

				cleaner.removeGym(gymId);

				return count != gymsRepository.count();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			Gyms _gyms = gymsRepository.findById(gymId).get();

			if (_gyms == null) {

				throw new Exception();

			}

			if (!_gyms.getGymTrainer().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

		long count = gymsRepository.count();

		cleaner.removeGym(gymId);

		return count != gymsRepository.count();
	}

}

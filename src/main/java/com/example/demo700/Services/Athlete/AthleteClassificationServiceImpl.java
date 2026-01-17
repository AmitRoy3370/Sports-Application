package com.example.demo700.Services.Athlete;

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
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;

@Service
public class AthleteClassificationServiceImpl implements AthleteClassificationService {

	@Autowired
	private AthleteClassificationRepository athleteClassificationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public AthleteClassification addAthleteClassification(AthleteClassification athleteClassification, String userId) {

		if (athleteClassification == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(athleteClassification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository
					.findByAthleteId(athleteClassification.getAthleteId());

			if (classification != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This athlete clasification is already added at here...");

		} catch (Exception e) {

		}

		athleteClassification = athleteClassificationRepository.save(athleteClassification);

		if (athleteClassification == null) {

			throw new ArithmeticException("No such athlete classification add at here...");

		}

		return athleteClassification;
	}

	@Override
	public AthleteClassification updateAthleteClassification(AthleteClassification athleteClassification, String userId,
			String athleteClassificationId) {

		if (athleteClassification == null || userId == null || athleteClassificationId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository.findById(athleteClassificationId)
					.get();

			if (classification == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(classification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(athleteClassification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!athlete.getUserId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository
					.findByAthleteId(athleteClassification.getAthleteId());

			if (classification != null) {

				if (!classification.getId().equals(athleteClassificationId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This athlete clasification is already added at here...");

		} catch (Exception e) {

		}

		athleteClassification.setId(athleteClassificationId);

		athleteClassification = athleteClassificationRepository.save(athleteClassification);

		if (athleteClassification == null) {

			throw new ArithmeticException("No such athlete classification add at here...");

		}

		return athleteClassification;
	}

	@Override
	public List<AthleteClassification> seeAll() {

		List<AthleteClassification> list = athleteClassificationRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such classification added at here....");

		}

		return list;
	}

	@Override
	public AthleteClassification findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			AthleteClassification athleteClassification = athleteClassificationRepository.findById(id).get();

			if (athleteClassification == null) {

				throw new Exception();

			}

			return athleteClassification;

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification add at here...");

		}
	}

	@Override
	public AthleteClassification findByAthleteId(String athleteId) {

		if (athleteId == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			AthleteClassification athleteClassification = athleteClassificationRepository.findByAthleteId(athleteId);

			if (athleteClassification == null) {

				throw new Exception();

			}

			return athleteClassification;

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification add at here...");

		}
	}

	@Override
	public List<AthleteClassification> findByAthleteClassificationTypes(
			AthleteClassificationTypes athleteClassificationTypes) {

		if (athleteClassificationTypes == null) {

			throw new NullPointerException("False request....");

		}

		List<AthleteClassification> list = athleteClassificationRepository
				.findByAthleteClassificationTypes(athleteClassificationTypes);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such classification added at here....");

		}

		return list;

	}

	@Override
	public boolean removeAthleteClassification(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			AthleteClassification classification = athleteClassificationRepository.findById(id).get();

			if (classification == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findById(classification.getAthleteId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = athleteClassificationRepository.count();

				cleaner.removeAthleteClassification(id);

				return count != athleteClassificationRepository.count();

			}

			if (!athlete.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such classification find at here...");

		}

		long count = athleteClassificationRepository.count();

		cleaner.removeAthleteClassification(id);

		return count != athleteClassificationRepository.count();
	}

}

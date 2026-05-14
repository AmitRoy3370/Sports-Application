package com.example.demo700.Services.DoctorServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.DoctorResponse;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.DoctorRepositories.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public Doctor addDoctor(String userId, Doctor doctor) {

		if (userId == null || doctor == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_DOCTOR)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			Doctor _doctor = doctorRepository.findByUserId(userId);

			if (_doctor != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("You already as a doctor...");

		} catch (Exception e) {

		}

		doctor = doctorRepository.save(doctor);

		return doctor;
	}

	@Override
	public List<DoctorResponse> seeAllDoctor() {

		List<Doctor> list = doctorRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here..");

		}

		return getDoctorResponse(list);
	}

	@Override
	public DoctorResponse findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Doctor doctor = doctorRepository.findByUserId(userId);

			if (doctor == null) {

				throw new Exception("No such doctor at here...");

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

	}

	@Override
	public List<DoctorResponse> findByYearOfExperiencesGreaterThan(int year) {

		List<Doctor> list = doctorRepository.findByYearOfExperiencesGreaterThan(year);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here...");

		}

		return getDoctorResponse(list);
	}

	@Override
	public List<DoctorResponse> findByDesignationIgnoreCase(String designation) {

		if (designation == null) {

			throw new NullPointerException("False request...");

		}

		List<Doctor> list = doctorRepository.findByDesignationIgnoreCase(designation);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here...");

		}

		return getDoctorResponse(list);
	}

	@Override
	public List<DoctorResponse> findByDegressContainingIgnoreCase(String degress) {

		if (degress == null) {

			throw new NullPointerException("False request...");

		}

		List<Doctor> list = doctorRepository.findByDegressContainingIgnoreCase(degress);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here..");

		}

		return getDoctorResponse(list);
	}

	@Override
	public List<DoctorResponse> findByWorkingExperiencesContainingIgnoreCase(String workingExperiences) {

		if (workingExperiences == null) {

			throw new NullPointerException("False request...");

		}

		List<Doctor> list = doctorRepository.findByWorkingExperiencesContainingIgnoreCase(workingExperiences);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here....");

		}

		return getDoctorResponse(list);
	}

	@Override
	public DoctorResponse findById(String doctorId) {

		if (doctorId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Doctor doctor = doctorRepository.findById(doctorId).get();

			if (doctor == null) {

				throw new Exception("No such doctor at here...");

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

	}

	@Override
	public Doctor updateDoctor(String userId, String doctorId, Doctor updatedDoctor) {

		if (userId == null || updatedDoctor == null | doctorId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_DOCTOR)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			Doctor doctor = doctorRepository.findById(doctorId).get();

			if (doctor == null) {

				throw new Exception();

			}

			if (!doctor.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor present at here...");

		}

		try {

			Doctor _doctor = doctorRepository.findByUserId(userId);

			if (_doctor != null) {

				if (!_doctor.getId().equals(doctorId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("You already as a doctor...");

		} catch (Exception e) {

		}

		updatedDoctor.setId(doctorId);

		updatedDoctor = doctorRepository.save(updatedDoctor);

		return updatedDoctor;
	}

	@Override
	public boolean deleteDoctor(String userId, String doctorId) {

		if (userId == null || doctorId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception("No such user exist at here...");

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = doctorRepository.count();

				cleaner.removeDoctor(doctorId);

				return count != doctorRepository.count();

			}

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

		try {

			Doctor doctor = doctorRepository.findById(doctorId).get();

			if (doctor == null) {

				throw new Exception("No such doctor exist at here...");

			}

			if (!doctor.getUserId().equals(userId)) {

				throw new Exception("You can only delete yourself...");

			}

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

		long count = doctorRepository.count();

		cleaner.removeDoctor(doctorId);

		return count != doctorRepository.count();
	}

	private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private DoctorResponse getDoctorResponse(Doctor doctor) {

		List<Doctor> list = new ArrayList<>();

		list.add(doctor);

		return getDoctorResponse(list).get(0);

	}

	private List<DoctorResponse> getDoctorResponse(List<Doctor> doctors) {

		List<DoctorResponse> responses = new ArrayList<>();

		List<String> allUserId = doctors.stream().map(Doctor::getUserId).collect(Collectors.toList());

		List<String> allDoctorId = doctors.stream().map(Doctor::getId).collect(Collectors.toList());

		List<User> users = userRepository.findAllById(allUserId);

		CompletableFuture<Map<String, String>> teamFuture = CompletableFuture.supplyAsync(() -> {

			List<Team> teams = teamRepository.findByDoctorsIn(allDoctorId);

			if (teams.isEmpty()) {

				return new HashMap<String, String>();

			}

			return teams.stream().filter(Objects::nonNull).filter(team -> team.getDoctors() != null)
					.flatMap(team -> team.getDoctors().stream().filter(Objects::nonNull)
							.map(doctorId -> Map.entry(doctorId, team.getTeamName())))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
							(existing, replacement) -> existing));

		}, executors);

		CompletableFuture<Map<String, User>> userFuture = CompletableFuture
				.supplyAsync(() -> users.isEmpty() ? new HashMap<>()
						: users.stream().filter(Objects::nonNull).filter(user -> user.getName() != null)
								.collect(Collectors.toMap(User::getId, Function.identity())),
						executors);

		CompletableFuture.allOf(userFuture, teamFuture).join();

		Map<String, User> userMap = userFuture.join();
		Map<String, String> teamMap = teamFuture.join();

		for (Doctor doctor : doctors) {

			try {

				DoctorResponse response = new DoctorResponse();

				response.setId(doctor.getId());
				response.setDegress(doctor.getDegress());
				response.setDesignation(doctor.getDesignation());
				response.setUserId(doctor.getUserId());
				response.setWorkingExperiences(doctor.getWorkingExperiences());

				try {

					response.setTeamName(teamMap.getOrDefault(doctor.getId(), "Un named"));

				} catch (Exception e) {

				}

				try {

					response.setUserName(userMap.get(doctor.getUserId()).getName());

				} catch (Exception e) {

					response.setUserName("Un named");

				}

				response.setYearOfExperiences(doctor.getYearOfExperiences());

				responses.add(response);

			} catch (Exception e) {

			}

		}

		return responses;

	}

}

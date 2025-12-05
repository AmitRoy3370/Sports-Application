package com.example.demo700.Services.DoctorServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.DoctorRepositories.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private UserRepository userRepository;

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
	public List<Doctor> seeAllDoctor() {

		List<Doctor> list = doctorRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here..");

		}

		return list;
	}

	@Override
	public Doctor findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Doctor doctor = doctorRepository.findByUserId(userId);

			if (doctor == null) {

				throw new Exception("No such doctor at here...");

			}

			return doctor;

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

	}

	@Override
	public List<Doctor> findByYearOfExperiencesGreaterThan(int year) {

		List<Doctor> list = doctorRepository.findByYearOfExperiencesGreaterThan(year);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here...");

		}

		return list;
	}

	@Override
	public List<Doctor> findByDesignationIgnoreCase(String designation) {

		if (designation == null) {

			throw new NullPointerException("False request...");

		}

		List<Doctor> list = doctorRepository.findByDesignationIgnoreCase(designation);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here...");

		}

		return list;
	}

	@Override
	public List<Doctor> findByDegressContainingIgnoreCase(String degress) {

		if (degress == null) {

			throw new NullPointerException("False request...");

		}

		List<Doctor> list = doctorRepository.findByDegressContainingIgnoreCase(degress);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here..");

		}

		return list;
	}

	@Override
	public List<Doctor> findByWorkingExperiencesContainingIgnoreCase(String workingExperiences) {

		if (workingExperiences == null) {

			throw new NullPointerException("False request...");

		}

		List<Doctor> list = doctorRepository.findByWorkingExperiencesContainingIgnoreCase(workingExperiences);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such doctor at here....");

		}

		return list;
	}

	@Override
	public Doctor findById(String doctorId) {

		if (doctorId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Doctor doctor = doctorRepository.findById(doctorId).get();

			if (doctor == null) {

				throw new Exception("No such doctor at here...");

			}

			return doctor;

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
			
			if(!doctor.getUserId().equals(userId)) {
				
				throw new Exception("You can only delete yourself...");
				
			}

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

		long count = doctorRepository.count();

		cleaner.removeDoctor(doctorId);

		return count != doctorRepository.count();
	}

}

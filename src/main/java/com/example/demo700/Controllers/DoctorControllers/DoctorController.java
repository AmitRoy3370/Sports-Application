package com.example.demo700.Controllers.DoctorControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Services.DoctorServices.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	// ✅ Add Doctor
	@PostMapping("/add/{userId}")
	public ResponseEntity<?> addDoctor(@PathVariable String userId, @RequestBody Doctor doctor) {
		try {
			Doctor savedDoctor = doctorService.addDoctor(userId, doctor);
			
			if(savedDoctor == null) {
				
				return ResponseEntity.status(500).body("Doctor is not added at here...");
				
			}
			
			return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// ✅ See All Doctors
	@GetMapping("/all")
	public ResponseEntity<?> getAllDoctors() {
		try {
			List<Doctor> doctors = doctorService.seeAllDoctor();
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ✅ Find By Doctor ID
	@GetMapping("/by-id/{doctorId}")
	public ResponseEntity<?> getDoctorById(@PathVariable String doctorId) {
		try {
			Doctor doctor = doctorService.findById(doctorId);
			return new ResponseEntity<>(doctor, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ✅ Find By User ID
	@GetMapping("/by-user/{userId}")
	public ResponseEntity<?> getDoctorByUserId(@PathVariable String userId) {
		try {
			Doctor doctor = doctorService.findByUserId(userId);
			
			if(doctor == null) {
				
				return ResponseEntity.status(500).body("Doctor is not updated...");
				
			}
			
			return new ResponseEntity<>(doctor, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ✅ Find By Experience Greater Than
	@GetMapping("/experience/{year}")
	public ResponseEntity<?> getDoctorsByExperience(@PathVariable int year) {
		try {
			List<Doctor> doctors = doctorService.findByYearOfExperiencesGreaterThan(year);
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ✅ Find By Designation
	@GetMapping("/designation/{designation}")
	public ResponseEntity<?> getDoctorsByDesignation(@PathVariable String designation) {
		try {
			List<Doctor> doctors = doctorService.findByDesignationIgnoreCase(designation);
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ✅ Find By Degree
	@GetMapping("/degree/{degree}")
	public ResponseEntity<?> getDoctorsByDegree(@PathVariable String degree) {
		try {
			List<Doctor> doctors = doctorService.findByDegressContainingIgnoreCase(degree);
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ✅ Find By Working Experience
	@GetMapping("/working/{work}")
	public ResponseEntity<?> getDoctorsByWorkingExperience(@PathVariable String work) {
		try {
			List<Doctor> doctors = doctorService.findByWorkingExperiencesContainingIgnoreCase(work);
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ✅ Update Doctor
	@PutMapping("/update/{userId}/{doctorId}")
	public ResponseEntity<?> updateDoctor(@PathVariable String userId, @PathVariable String doctorId,
			@RequestBody Doctor updatedDoctor) {
		try {
			Doctor doctor = doctorService.updateDoctor(userId, doctorId, updatedDoctor);
			return new ResponseEntity<>(doctor, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// ✅ Delete Doctor
	@DeleteMapping("/delete/{userId}/{doctorId}")
	public ResponseEntity<?> deleteDoctor(@PathVariable String userId, @PathVariable String doctorId) {
		try {
			boolean deleted = doctorService.deleteDoctor(userId, doctorId);

			if (deleted) {
				return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Doctor deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}

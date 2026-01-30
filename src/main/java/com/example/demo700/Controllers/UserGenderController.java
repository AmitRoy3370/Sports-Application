package com.example.demo700.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.ENUMS.Gender;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Models.Turf.Owner;
import com.example.demo700.Services.UserGenderService;

@RestController
@RequestMapping("/api/user-gender")
public class UserGenderController {

	@Autowired
	private UserGenderService userGenderService;

	// ================= ADD =================
	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody UserGender userGender) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(userGenderService.addUserGender(userGender));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// ================= UPDATE =================
	@PutMapping("/update/{id}/{userId}")
	public ResponseEntity<?> update(@RequestBody UserGender userGender, @PathVariable String id,
			@PathVariable String userId) {
		try {
			return ResponseEntity.ok(userGenderService.updateUserGender(userGender, userId, id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// ================= FIND BY ID =================
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		try {
			return ResponseEntity.ok(userGenderService.findById(id));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= SEE ALL =================
	@GetMapping("/all")
	public ResponseEntity<?> seeAll() {
		try {
			return ResponseEntity.ok(userGenderService.seeAll());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= FIND BY USER ID =================
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findByUserId(@PathVariable String userId) {
		try {
			return ResponseEntity.ok(userGenderService.findByUserId(userId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= FIND BY GENDER =================
	@GetMapping("/gender/{gender}")
	public ResponseEntity<?> findByGender(@PathVariable Gender gender) {
		try {
			return ResponseEntity.ok(userGenderService.findByGender(gender));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= DELETE =================
	@DeleteMapping("/{id}/{userId}")
	public ResponseEntity<?> delete(@PathVariable String id, @PathVariable String userId) {
		try {
			boolean deleted = userGenderService.deleteUserGender(id, userId);
			return ResponseEntity.ok(deleted);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// ================= ATHLETES =================
	@GetMapping("/athletes/{gender}")
	public ResponseEntity<?> findAllAthletes(@PathVariable Gender gender) {
		try {
			List<Athelete> list = userGenderService.findAllAthlete(gender);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= COACHES =================
	@GetMapping("/coaches/{gender}")
	public ResponseEntity<?> findAllCoaches(@PathVariable Gender gender) {
		try {
			List<Coach> list = userGenderService.findAllCoach(gender);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= SCOUTS =================
	@GetMapping("/scouts/{gender}")
	public ResponseEntity<?> findAllScouts(@PathVariable Gender gender) {
		try {
			List<Scouts> list = userGenderService.findAllScouts(gender);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= VENUE OWNERS =================
	@GetMapping("/venue-owners/{gender}")
	public ResponseEntity<?> findAllVenueOwners(@PathVariable Gender gender) {
		try {
			List<Owner> list = userGenderService.findAllVenueOwner(gender);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= GYM TRAINERS =================
	@GetMapping("/gym-trainers/{gender}")
	public ResponseEntity<?> findAllGymTrainers(@PathVariable Gender gender) {
		try {
			List<User> list = userGenderService.findAllGymTrainer(gender);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= GYM OWNERS =================
	@GetMapping("/gym-owners/{gender}")
	public ResponseEntity<?> findAllGymOwners(@PathVariable Gender gender) {
		try {
			List<User> list = userGenderService.findAllGymOwner(gender);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ================= TEAM OWNERS =================
	@GetMapping("/team-owners/{gender}")
	public ResponseEntity<?> findAllTeamOwners(@PathVariable Gender gender) {
		try {
			List<TeamOwner> list = userGenderService.findAllTeamOwner(gender);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// =================== Doctors =====================

	@GetMapping("/doctos/{gender}")
	public ResponseEntity<?> findAllDoctors(@PathVariable Gender gender) {

		try {

			List<Doctor> list = userGenderService.findAllDoctor(gender);

			return ResponseEntity.ok(list);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

		}

	}

}

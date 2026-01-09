package com.example.demo700.Controllers.GymController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.GymModels.Gyms;
import com.example.demo700.Services.GymServices.GymService;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

	@Autowired
	private GymService gymService;

	// ✅ ADD GYM
	@PostMapping
	public ResponseEntity<?> addGym(@RequestBody Gyms gyms) {

		try {

			Gyms saved = gymService.addGyms(gyms);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	// ✅ UPDATE GYM
	@PutMapping("/{gymId}/{userId}")
	public ResponseEntity<?> updateGym(@RequestBody Gyms gyms, @PathVariable String gymId,
			@PathVariable String userId) {

		try {

			Gyms updated = gymService.updateGyms(gyms, userId, gymId);
			return ResponseEntity.ok(updated);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	// ✅ FIND BY ID
	@GetMapping("/id/{gymId}")
	public ResponseEntity<?> findById(@PathVariable String gymId) {

		try {

			return ResponseEntity.ok(gymService.findById(gymId));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ FIND BY TRAINER
	@GetMapping("/trainer/{trainerId}")
	public ResponseEntity<?> findByTrainer(@PathVariable String trainerId) {

		try {

			List<Gyms> list = gymService.findByGymTrainer(trainerId);
			return ResponseEntity.ok(list);

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ FIND BY GYM NAME
	@GetMapping("/name/{gymName}")
	public ResponseEntity<?> findByName(@PathVariable String gymName) {

		try {

			return ResponseEntity.ok(gymService.findByGymName(gymName));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ FIND BY LOCATION
	@GetMapping("/location/{location}")
	public ResponseEntity<?> findByLocation(@PathVariable String location) {

		try {

			return ResponseEntity.ok(gymService.findByLocationName(location));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ FIND BY COORDINATES
	@GetMapping("/coordinates")
	public ResponseEntity<?> findByCoordinates(@RequestParam double latitude, @RequestParam double longtitude) {

		try {

			return ResponseEntity.ok(gymService.findByLatitudeAndLongtitude(latitude, longtitude));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ FILTER BY ENTRY FEE
	@GetMapping("/entry-fee")
	public ResponseEntity<?> findByEntryFee(@RequestParam double maxFee) {
		return ResponseEntity.ok(gymService.findByEntryFeeLessThanOrEqual(maxFee));
	}

	// ✅ FILTER BY MONTHLY FEE
	@GetMapping("/monthly-fee")
	public ResponseEntity<?> findByMonthlyFee(@RequestParam double maxFee) {

		try {

			return ResponseEntity.ok(gymService.findByMonthlyFeeLessThanOrEqual(maxFee));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ SEE ALL GYMS
	@GetMapping("/all")
	public ResponseEntity<?> seeAll() {

		try {

			return ResponseEntity.ok(gymService.seeAllGyms());

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ DELETE GYM
	@DeleteMapping("/{gymId}/{userId}")
	public ResponseEntity<?> deleteGym(@PathVariable String gymId, @PathVariable String userId) {

		try {

			boolean deleted = gymService.deleteGyms(gymId, userId);
			return ResponseEntity.ok(deleted);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}
}

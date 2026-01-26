package com.example.demo700.Controllers.GymController;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.Models.GymModels.Gyms;
import com.example.demo700.Services.GymServices.GymService;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

	@Autowired
	private GymService gymService;

	// ✅ ADD GYM
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addGym(@RequestPart("gymTrainer") String gymTrainer,
			@RequestPart("gymOwner") String gymOwner,
			@RequestPart(value = "tradeLicenseId", required = false) String tradeLicenseId,
			@RequestPart("tinNumber") String tinNumber,
			@RequestPart(value = "openingTime", required = false) String openingTime,
			@RequestPart(value = "closingTime", required = false) String closingTime,
			@RequestPart("gymName") String gymName, @RequestPart("locationName") String locationName,
			@RequestPart("latitude") double latitude, @RequestPart("longtitude") double longtitude,
			@RequestPart("entryFee") double entryFee, @RequestPart("monthlyFee") double monthlyFee,
			@RequestPart(value = "CoverImage", required = false) MultipartFile coverImage,
			@RequestPart(value = "attachments", required = false) MultipartFile files[], @RequestParam String userId) {

		try {

			Gyms gyms = new Gyms();

			gyms.setGymTrainer(gymTrainer);
			gyms.setGymOwner(gymOwner);

			if (tradeLicenseId != null) {

				gyms.setTradeLicenseId(tradeLicenseId);

			}

			if (tinNumber != null) {

				gyms.setTinNumber(tinNumber);

			}

			if (openingTime != null) {

				gyms.setOpeningTime(Instant.parse(openingTime));

			}

			if (closingTime != null) {

				gyms.setClosingTime(Instant.parse(closingTime));

			}

			gyms.setEntryFee(entryFee);
			gyms.setMonthlyFee(monthlyFee);

			gyms.setGymName(gymName);
			gyms.setLatitude(latitude);
			gyms.setLongtitude(longtitude);

			Gyms saved = gymService.addGyms(gyms, userId, files, coverImage);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	// ✅ UPDATE GYM
	@PutMapping(value = "/{gymId}/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateGym(@RequestPart("gymTrainer") String gymTrainer,
			@RequestPart("gymOwner") String gymOwner,
			@RequestPart(value = "tradeLicenseId", required = false) String tradeLicenseId,
			@RequestPart("tinNumber") String tinNumber,
			@RequestPart(value = "openingTime", required = false) String openingTime,
			@RequestPart(value = "closingTime", required = false) String closingTime,
			@RequestPart("gymName") String gymName, @RequestPart("locationName") String locationName,
			@RequestPart("latitude") double latitude, @RequestPart("longtitude") double longtitude,
			@RequestPart("entryFee") double entryFee, @RequestPart("monthlyFee") double monthlyFee,
			@RequestPart(value = "CoverImage", required = false) MultipartFile coverImage,
			@RequestPart(value = "attachments", required = false) MultipartFile files[], @PathVariable String gymId,
			@RequestPart(value = "existingFiles", required = false) List<String> existingFiles,
			@PathVariable String userId) {

		try {

			Gyms gyms = new Gyms();

			gyms.setGymTrainer(gymTrainer);
			gyms.setGymOwner(gymOwner);

			if (tradeLicenseId != null) {

				gyms.setTradeLicenseId(tradeLicenseId);

			}

			if (tinNumber != null) {

				gyms.setTinNumber(tinNumber);

			}

			if (openingTime != null) {

				gyms.setOpeningTime(Instant.parse(openingTime));

			}

			if (closingTime != null) {

				gyms.setClosingTime(Instant.parse(closingTime));

			}

			if (existingFiles != null && !existingFiles.isEmpty()) {

				gyms.setGymImages(existingFiles);

			}

			gyms.setEntryFee(entryFee);
			gyms.setMonthlyFee(monthlyFee);

			gyms.setGymName(gymName);
			gyms.setLatitude(latitude);
			gyms.setLongtitude(longtitude);

			Gyms updated = gymService.updateGyms(gyms, userId, gymId, files, coverImage);
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

	// ✅ FIND BY GYM OWNER
	@GetMapping("/owner/{ownerId}")
	public ResponseEntity<?> findByGymOwner(@PathVariable String ownerId) {
		try {
			return ResponseEntity.ok(gymService.findByGymOwner(ownerId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ✅ SEARCH GYM NAME (contains, ignore case)
	@GetMapping("/search")
	public ResponseEntity<?> searchByGymName(@RequestParam String name) {
		try {
			return ResponseEntity.ok(gymService.findByGymNameContainingIgnoreCase(name));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ✅ FIND BY TRADE LICENSE ID
	@GetMapping("/trade-license/{tradeLicenseId}")
	public ResponseEntity<?> findByTradeLicense(@PathVariable String tradeLicenseId) {
		try {
			return ResponseEntity.ok(gymService.findByTradeLicenseId(tradeLicenseId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ✅ FIND BY TIN NUMBER
	@GetMapping("/tin/{tinNumber}")
	public ResponseEntity<?> findByTinNumber(@PathVariable String tinNumber) {
		try {
			return ResponseEntity.ok(gymService.findByTinNumber(tinNumber));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ✅ OPENING TIME BEFORE
	@GetMapping("/opening-time/before")
	public ResponseEntity<?> openingTimeBefore(@RequestParam Instant time) {
		try {
			return ResponseEntity.ok(gymService.findByOpeningTimeBefore(time));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ✅ OPENING TIME AFTER
	@GetMapping("/opening-time/after")
	public ResponseEntity<?> openingTimeAfter(@RequestParam Instant time) {
		try {
			return ResponseEntity.ok(gymService.findByOpeningTimeAfter(time));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ✅ CLOSING TIME BEFORE
	@GetMapping("/closing-time/before")
	public ResponseEntity<?> closingTimeBefore(@RequestParam Instant time) {
		try {
			return ResponseEntity.ok(gymService.findByClosingTimeBefore(time));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ✅ CLOSING TIME AFTER
	@GetMapping("/closing-time/after")
	public ResponseEntity<?> closingTimeAfter(@RequestParam Instant time) {
		try {
			return ResponseEntity.ok(gymService.findByClosingTimeAfter(time));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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

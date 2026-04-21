package com.example.demo700.Controllers.AtheleteController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.DTOFiles.ScoutResponse;
import com.example.demo700.DTOFiles.ScoutsListResponseDTO;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Services.Athlete.ScoutService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/scout")
public class ScoutController {

	@Autowired
	private ScoutService scoutService;

	@PostMapping("/addScout")
	public ResponseEntity<?> addScouts(@RequestBody Scouts scout, @RequestParam String userId) {

		try {

			scout = scoutService.addScout(scout, userId);

			if (scout == null) {

				return ResponseEntity.status(500).body("Scout is not added at here...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(201).body(scout);

	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllScouts() {

		List<ScoutResponse> list = scoutService.seeAllScouts();

		if (list == null) {

			return ResponseEntity.status(404).body("No scout's find at here...");

		}

		return ResponseEntity.status(200).body(list);

	}

	@GetMapping("/searchByAtheleteId")
	public ResponseEntity<?> findByAtheleteId(@RequestParam String atheleteId) {

		try {

			ScoutResponse scouts = scoutService.findByAtheleteId(atheleteId);

			if (scouts == null) {

				return ResponseEntity.status(404).body("No such scout's find at here..");

			}

			return ResponseEntity.status(200).body(scouts);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam String id) {

		try {

			return ResponseEntity.status(200).body(scoutService.findByScoutsId(id));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	// ✅ Update existing scout
	@PutMapping("/updateScout")
	public ResponseEntity<?> updateScouts(@RequestParam String scoutId, @RequestParam String userId,
			@RequestBody Scouts scout) {
		try {
			Scouts updated = scoutService.updateScouts(scoutId, userId, scout);
			if (updated == null) {
				return ResponseEntity.status(500).body("Scout is not updated properly...");
			}
			return ResponseEntity.status(200).body(updated);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	// ✅ Delete a scout
	@DeleteMapping("/deleteScout")
	public ResponseEntity<?> deleteScouts(@RequestParam String scoutId, @RequestParam String userId) {
		try {
			boolean deleted = scoutService.deleteScouts(scoutId, userId);
			if (!deleted) {
				return ResponseEntity.status(404).body("No such scout found or deletion failed...");
			}
			return ResponseEntity.status(200).body("Scout deleted successfully...");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	// ✅ Find scouts by event participation
	@GetMapping("/searchByEvent")
	public ResponseEntity<?> findByEvent(@RequestParam String eventId) {
		try {
			List<ScoutResponse> list = scoutService.findByEventsContainingIgnoreCase(eventId);
			if (list == null || list.isEmpty()) {
				return ResponseEntity.status(404).body("No scouts found for this event...");
			}
			return ResponseEntity.status(200).body(list);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	// ✅ Find scouts by match participation
	@GetMapping("/searchByMatch")
	public ResponseEntity<?> findByMatch(@RequestParam String matchId) {
		try {
			List<ScoutResponse> list = scoutService.findByMatchesContainingIgnoreCase(matchId);
			if (list == null || list.isEmpty()) {
				return ResponseEntity.status(404).body("No scouts found for this match...");
			}
			return ResponseEntity.status(200).body(list);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@GetMapping("/findByClassification")
	public ResponseEntity<?> findByAthleteClassification(@RequestParam String classification) {

		try {

			return ResponseEntity.status(200)
					.body(scoutService.findByAthleteClassification(AthleteClassificationTypes.valueOf(classification)));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	/**
	 * ✅ Get all scouts with pagination
	 */
	@GetMapping("/seeAllPaginated")
	public ResponseEntity<?> seeAllScoutsPaginated(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			String baseUrl = request.getRequestURL().toString();
			ScoutsListResponseDTO response = scoutService.findAllScoutsPaginated(pageable, baseUrl);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	/**
	 * ✅ Search scouts by athlete ID with pagination
	 */
	@GetMapping("/searchByAtheleteIdPaginated")
	public ResponseEntity<?> findByAtheleteIdPaginated(@RequestParam String atheleteId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			String baseUrl = request.getRequestURL().toString();
			ScoutsListResponseDTO response = scoutService.searchByAtheleteIdPaginated(atheleteId, pageable, baseUrl);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	/**
	 * ✅ Search scouts by scout ID with pagination
	 */
	@GetMapping("/findByIdPaginated")
	public ResponseEntity<?> findByIdPaginated(@RequestParam String id, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			String baseUrl = request.getRequestURL().toString();
			ScoutsListResponseDTO response = scoutService.searchByScoutsIdPaginated(id, pageable, baseUrl);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	/**
	 * ✅ Search scouts by event with pagination
	 */
	@GetMapping("/searchByEventPaginated")
	public ResponseEntity<?> findByEventPaginated(@RequestParam String eventId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			String baseUrl = request.getRequestURL().toString();
			ScoutsListResponseDTO response = scoutService.searchByEventsContainingPaginated(eventId, pageable, baseUrl);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	/**
	 * ✅ Search scouts by match with pagination
	 */
	@GetMapping("/searchByMatchPaginated")
	public ResponseEntity<?> findByMatchPaginated(@RequestParam String matchId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			String baseUrl = request.getRequestURL().toString();
			ScoutsListResponseDTO response = scoutService.searchByMatchesContainingPaginated(matchId, pageable,
					baseUrl);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	/**
	 * ✅ Search scouts by classification with pagination
	 */
	@GetMapping("/findByClassificationPaginated")
	public ResponseEntity<?> findByAthleteClassificationPaginated(@RequestParam String classification,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			String baseUrl = request.getRequestURL().toString();
			ScoutsListResponseDTO response = scoutService.searchByClassificationPaginated(
					AthleteClassificationTypes.valueOf(classification), pageable, baseUrl);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	/**
	 * ✅ Search scouts by name with pagination
	 */
	@GetMapping("/searchByNamePaginated")
	public ResponseEntity<?> searchByNamePaginated(@RequestParam String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			String baseUrl = request.getRequestURL().toString();
			ScoutsListResponseDTO response = scoutService.searchByNamePaginated(name, pageable, baseUrl);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

}

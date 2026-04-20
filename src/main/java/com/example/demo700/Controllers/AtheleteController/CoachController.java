package com.example.demo700.Controllers.AtheleteController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.DTOFiles.CoachListResponseDTO;
import com.example.demo700.DTOFiles.CoachResponse;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Services.Athlete.CoachService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/coach")
public class CoachController {

	@Autowired
	private CoachService coachService;

	@PostMapping("/addCoach")
	public ResponseEntity<?> addCoach(@RequestBody Coach coach, @RequestParam String userId) {

		try {

			coach = coachService.addCoach(coach, userId);

			if (coach == null) {

				return ResponseEntity.status(500).body("Coach is not added...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(201).body(e.getMessage());

		}

		return ResponseEntity.status(201).body(coach);

	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllCoach() {

		List<CoachResponse> list = coachService.seeAll();

		if (list == null) {

			return ResponseEntity.status(404).body("No Coach find at here...");

		}

		return ResponseEntity.status(200).body(list);

	}

	// ✅ New paginated endpoint
	@GetMapping("/paginated")
	public ResponseEntity<?> getAllCoachesPaginated(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
		try {
			String requestUrl = request.getRequestURL().toString();
			CoachListResponseDTO response = coachService.seeAllPaginated(page, size, requestUrl);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	// ✅ New paginated classification endpoint
	@GetMapping("/classification/paginated")
	public ResponseEntity<?> findByCoachClassificationPaginated(@RequestParam String type,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {
		try {
			AthleteClassificationTypes classificationType = AthleteClassificationTypes.valueOf(type.toUpperCase());
			String requestUrl = request.getRequestURL().toString();
			CoachListResponseDTO response = coachService.findByCoachClassificationPaginated(classificationType, page,
					size, requestUrl);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Invalid classification type");
		} catch (Exception e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	// ✅ New search by name endpoint
	@GetMapping("/search/name")
	public ResponseEntity<?> searchByCoachName(@RequestParam String name, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
		try {
			String requestUrl = request.getRequestURL().toString();
			CoachListResponseDTO response = coachService.searchByCoachName(name, page, size, requestUrl);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	// ✅ New search by team name endpoint
	@GetMapping("/search/team")
	public ResponseEntity<?> searchByTeamName(@RequestParam String teamName, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
		try {
			String requestUrl = request.getRequestURL().toString();
			CoachListResponseDTO response = coachService.searchByTeamName(teamName, page, size, requestUrl);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchCoach(@RequestParam String coachId) {

		CoachResponse coach = coachService.searchCoach(coachId);

		if (coach == null) {

			return ResponseEntity.status(404).body("No coach find at here...");

		}

		return ResponseEntity.status(200).body(coach);

	}

	@GetMapping("/findByAthleteId")
	public ResponseEntity<?> findByAthleteId(@RequestParam String athleteId) {

		try {

			return ResponseEntity.status(200).body(coachService.findByAthleteId(athleteId));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam String id) {

		try {

			return ResponseEntity.status(200).body(coachService.findByCoachId(id));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateCoach(@RequestBody Coach coach, @RequestParam String userId,
			@RequestParam String coachId) {

		try {

			coach = coachService.updateCoach(coach, userId, coachId);

			if (coach == null) {

				return ResponseEntity.status(500).body("Coach is not updated at here..");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(200).body(coach);

	}

	@GetMapping("/findByClassification/{classification}")
	public ResponseEntity<?> findByCoachClassification(@PathVariable AthleteClassificationTypes classification) {

		try {

			return ResponseEntity.status(200).body(coachService.findByCoachClassification(classification));

		} catch (Exception e) {

			return ResponseEntity.status(404).body(e.getMessage());

		}

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCoach(@RequestParam String coachId, @RequestParam String userId) {

		try {

			boolean yes = coachService.deleteCoach(coachId, userId);

			if (!yes) {

				return ResponseEntity.status(500).body("Coach is not deleted...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(200).body("Coach deleted...");

	}

}

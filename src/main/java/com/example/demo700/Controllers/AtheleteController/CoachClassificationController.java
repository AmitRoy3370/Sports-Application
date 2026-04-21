package com.example.demo700.Controllers.AtheleteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.DTOFiles.CoachListResponseDTO;
import com.example.demo700.DTOFiles.CoachResponse;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.CoachClassification;
import com.example.demo700.Services.Athlete.CoachClassificationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/coach-classification")
public class CoachClassificationController {

	@Autowired
	private CoachClassificationService coachClassificationService;

	// ==================== CREATE ====================
	@PostMapping
	public ResponseEntity<?> addClassification(
			@RequestBody CoachClassification coachClassification,
			@RequestParam String userId) {

		try {
			CoachClassification response =
					coachClassificationService.addAthleteClassification(coachClassification, userId);

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (ArithmeticException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ==================== UPDATE ====================
	@PutMapping("/{id}")
	public ResponseEntity<?> updateClassification(
			@PathVariable String id,
			@RequestBody CoachClassification coachClassification,
			@RequestParam String userId) {

		try {
			CoachClassification response =
					coachClassificationService.updateAthleteClassification(coachClassification, userId, id);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (ArithmeticException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ==================== GET ALL ====================
	@GetMapping
	public ResponseEntity<?> getAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {

		try {
			String requestUrl = request.getRequestURL().toString()
					+ (request.getQueryString() != null ? "?" + request.getQueryString() : "");

			CoachListResponseDTO response =
					coachClassificationService.seeAll(page, size, requestUrl);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ==================== GET BY ID ====================
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) {

		try {
			CoachResponse response = coachClassificationService.findById(id);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ==================== GET BY COACH ID ====================
	@GetMapping("/coach/{coachId}")
	public ResponseEntity<?> getByCoachId(@PathVariable String coachId) {

		try {
			CoachResponse response = coachClassificationService.findByCoachId(coachId);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ==================== FILTER ====================
	@GetMapping("/type/{type}")
	public ResponseEntity<?> getByType(
			@PathVariable AthleteClassificationTypes type,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {

		try {
			String requestUrl = request.getRequestURL().toString()
					+ (request.getQueryString() != null ? "?" + request.getQueryString() : "");

			CoachListResponseDTO response =
					coachClassificationService.findByAthleteClassificationTypes(type, page, size, requestUrl);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// ==================== DELETE ====================
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteClassification(
			@PathVariable String id,
			@RequestParam String userId) {

		try {
			boolean deleted =
					coachClassificationService.removeAthleteClassification(id, userId);

			if (deleted) {
				return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
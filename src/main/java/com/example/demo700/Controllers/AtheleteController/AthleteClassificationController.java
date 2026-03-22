package com.example.demo700.Controllers.AtheleteController;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Services.Athlete.AthleteClassificationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/athlete-classification")
public class AthleteClassificationController {

	@Autowired
	private AthleteClassificationService athleteClassificationService;

	// Auto-pagination settings
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;
	private static final int MAX_SIZE = 50;

	// ======================================
	// ADD CLASSIFICATION
	// ======================================
	@PostMapping("/add/{userId}")
	public ResponseEntity<?> addAthleteClassification(@RequestBody AthleteClassification athleteClassification,
			@PathVariable String userId) {

		try {
			AthleteClassification saved = athleteClassificationService.addAthleteClassification(athleteClassification,
					userId);

			return ResponseEntity.status(HttpStatus.CREATED).body(saved);

		} catch (NullPointerException | NoSuchElementException | ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unexpected error occurred while adding athlete classification");
		}
	}

	// ======================================
	// UPDATE CLASSIFICATION
	// ======================================
	@PutMapping("/update/{classificationId}/{userId}")
	public ResponseEntity<?> updateAthleteClassification(@RequestBody AthleteClassification athleteClassification,
			@PathVariable String classificationId, @PathVariable String userId) {

		try {
			AthleteClassification updated = athleteClassificationService
					.updateAthleteClassification(athleteClassification, userId, classificationId);

			return ResponseEntity.ok(updated);

		} catch (NullPointerException | NoSuchElementException | ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unexpected error occurred while updating athlete classification");
		}
	}

	// ======================================
	// GET ALL
	// ======================================
	@GetMapping("/all")
	public ResponseEntity<?> seeAll(HttpServletRequest request) {

		try {
			return handleAutoPagination(request, null, null, null);

		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// ======================================
	// FIND BY ID
	// ======================================
	@GetMapping("/id/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {

		try {
			AthleteRequestDTO classification = athleteClassificationService.findById(id);

			return ResponseEntity.ok(classification);

		} catch (NullPointerException | NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// ======================================
	// FIND BY ATHLETE ID
	// ======================================
	@GetMapping("/athlete/{athleteId}")
	public ResponseEntity<?> findByAthleteId(@PathVariable String athleteId) {

		try {
			AthleteRequestDTO classification = athleteClassificationService.findByAthleteId(athleteId);

			return ResponseEntity.ok(classification);

		} catch (NullPointerException | NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// ======================================
	// FIND BY CLASSIFICATION TYPE (ENUM)
	// ======================================
	@GetMapping("/type/{type}")
	public ResponseEntity<?> findByAthleteClassificationTypes(@PathVariable AthleteClassificationTypes type,
			HttpServletRequest request) {

		try {
			return handleAutoPagination(request, null, null, null);

		} catch (NullPointerException | NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// ======================================
	// DELETE CLASSIFICATION
	// ======================================
	@DeleteMapping("/delete/{id}/{userId}")
	public ResponseEntity<?> removeAthleteClassification(@PathVariable String id, @PathVariable String userId) {

		try {
			boolean deleted = athleteClassificationService.removeAthleteClassification(id, userId);

			return ResponseEntity.ok("Deleted : " + deleted);

		} catch (NullPointerException | NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unexpected error occurred while deleting athlete classification");
		}
	}

// ==================== PAGINATION NAVIGATION ENDPOINTS ====================

	@GetMapping("/next")
	public ResponseEntity<?> nextPage(@RequestParam String currentUrl,
			@RequestParam(required = false) String searchType, @RequestParam(required = false) String searchValue,
			HttpServletRequest request) {

		try {
			// Extract current page from URL
			int currentPage = extractPageFromUrl(currentUrl);
			int nextPage = currentPage + 1;

			return handleAutoPagination(request, searchType, searchValue, nextPage);
		} catch (Exception e) {
			return ResponseEntity.status(400).body("Error navigating to next page: " + e.getMessage());
		}
	}

	@GetMapping("/prev")
	public ResponseEntity<?> prevPage(@RequestParam String currentUrl,
			@RequestParam(required = false) String searchType, @RequestParam(required = false) String searchValue,
			HttpServletRequest request) {

		try {
			int currentPage = extractPageFromUrl(currentUrl);
			int prevPage = Math.max(0, currentPage - 1);

			return handleAutoPagination(request, searchType, searchValue, prevPage);
		} catch (Exception e) {
			return ResponseEntity.status(400).body("Error navigating to previous page: " + e.getMessage());
		}
	}

	private ResponseEntity<?> handleAutoPagination(HttpServletRequest request, String searchType, String searchValue,
			Integer specificPage) {

		try {

			int page = specificPage != null ? specificPage : detectPageFromRequest(request);
			int size = detectSizeFromRequest(request);

			// Validate page and size
			page = Math.max(0, page);
			size = Math.min(MAX_SIZE, Math.max(1, size));

			AthleteListResponseDTO response = null;

			if (searchType == null || searchValue == null) {

				response = athleteClassificationService.seeAll(page, size);

			} else {

				switch (searchType) {

				case "all":
					response = athleteClassificationService.seeAll(page, size);
					break;
				case "type":
					response = athleteClassificationService.findByAthleteClassificationTypes(
							AthleteClassificationTypes.valueOf(searchValue), page, size);
					break;
				default:
					response = athleteClassificationService.seeAll(page, size);
				}

			}

			if (response.getAthletes().isEmpty()) {
				return ResponseEntity.status(404).body("No athletes found");
			}

			// Add navigation links
			addNavigationLinks(response, request, searchType, searchValue);

			response.setMessage("Auto-pagination applied. Use nextLink/prevLink for navigation.");
			response.setSuggestedPageSize(String.valueOf(DEFAULT_SIZE));
			response.setCurrentUrl(request.getRequestURI());

			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {

			return ResponseEntity.status(400).body("Error: " + e.getMessage());

		}

	}

	private void addNavigationLinks(AthleteListResponseDTO response, HttpServletRequest request, String searchType,
			String searchValue) {

		String baseUrl = request.getRequestURI();
		int currentPage = response.getCurrentPage();
		int pageSize = response.getPageSize();
		int totalPages = response.getTotalPages();

		// Build navigation links
		response.setSelfLink(baseUrl + "?page=" + currentPage + "&size=" + pageSize);

		if (currentPage < totalPages - 1) {
			response.setNextLink("/api/athlete-classification/next?currentUrl=" + response.getSelfLink()
					+ "&searchType=" + (searchType != null ? searchType : "") + "&searchValue="
					+ (searchValue != null ? searchValue : ""));
		}

		if (currentPage > 0) {
			response.setPrevLink("/api/athlete-classification/prev?currentUrl=" + response.getSelfLink()
					+ "&searchType=" + (searchType != null ? searchType : "") + "&searchValue="
					+ (searchValue != null ? searchValue : ""));
		}

		response.setFirstLink(baseUrl + "?page=0&size=" + pageSize);

		if (totalPages > 0) {
			response.setLastLink(baseUrl + "?page=" + (totalPages - 1) + "&size=" + pageSize);
		}

	}

	// ========================================
	// HELPER METHOD
	// ========================================

	private int detectPageFromRequest(HttpServletRequest request) {

		String pageParam = request.getParameter("page");

		if (pageParam != null) {

			try {

				return Integer.parseInt(pageParam);

			} catch (Exception e) {

				return DEFAULT_PAGE;

			}

		}

		return DEFAULT_PAGE;

	}

	private int detectSizeFromRequest(HttpServletRequest request) {

		String sizeParam = request.getParameter("size");

		if (sizeParam != null) {

			try {

				return Integer.parseInt(sizeParam);

			} catch (Exception e) {

				return DEFAULT_SIZE;

			}

		}

		return DEFAULT_SIZE;

	}

	private int extractPageFromUrl(String url) {
		try {
			if (url.contains("page=")) {
				String pagePart = url.substring(url.indexOf("page=") + 5);
				if (pagePart.contains("&")) {
					pagePart = pagePart.substring(0, pagePart.indexOf("&"));
				}
				return Integer.parseInt(pagePart);
			}
		} catch (Exception e) {
			// Fallback to default
		}
		return DEFAULT_PAGE;
	}

}

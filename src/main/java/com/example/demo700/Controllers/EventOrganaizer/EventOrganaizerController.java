package com.example.demo700.Controllers.EventOrganaizer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Services.EventOrganaizer.EventOrganaizerService;

@RestController
@RequestMapping("/api/event-organizer")
public class EventOrganaizerController {

	@Autowired
	private EventOrganaizerService eventOrganaizerService;

	/**
	 * ✅ Add new Event Organizer
	 */
	@PostMapping("/add/{userId}")
	public ResponseEntity<?> addEventOrganaizer(@PathVariable String userId,
			@RequestBody EventOrganaizer eventOrganaizer) {
		try {
			EventOrganaizer savedOrg = eventOrganaizerService.addEventOrganaizer(eventOrganaizer, userId);
			return new ResponseEntity<>(savedOrg, HttpStatus.CREATED);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ArithmeticException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>("Something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * ✅ Get all Event Organizers
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllEventOrganizers() {
		try {
			List<EventOrganaizer> organizers = eventOrganaizerService.seeAll();
			return new ResponseEntity<>(organizers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to fetch organizers: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * ✅ Get Event Organizer by userId
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getByUserId(@PathVariable String userId) {
		try {
			EventOrganaizer organizer = eventOrganaizerService.findByUserId(userId);
			return new ResponseEntity<>(organizer, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ✅ Get Event Organizer by organization name
	 */
	@GetMapping("/name/{organaizationName}")
	public ResponseEntity<?> getByOrganizationName(@PathVariable String organaizationName) {
		try {
			EventOrganaizer organizer = eventOrganaizerService.findByOrganaizationName(organaizationName);
			return new ResponseEntity<>(organizer, HttpStatus.OK);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam String id) {
		
		try {
			
			return ResponseEntity.status(200).body(eventOrganaizerService.findByEventOrganaizerId(id));
			
		} catch(Exception e) {
			
			return ResponseEntity.status(404).body(e.getMessage());
			
		}
		
	}
	
	/**
	 * ✅ Get all Event Organizers that include a specific match
	 */
	@GetMapping("/match/{matchId}")
	public ResponseEntity<?> getByMatchId(@PathVariable String matchId) {
		try {
			List<EventOrganaizer> organizers = eventOrganaizerService.findByMatchesContainingIgnoreCase(matchId);
			return new ResponseEntity<>(organizers, HttpStatus.OK);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * ✅ Update Event Organizer information
	 */
	@PutMapping("/update/{userId}/{eventOrganaizerId}")
	public ResponseEntity<?> updateEventOrganaizer(@PathVariable String userId, @PathVariable String eventOrganaizerId,
			@RequestBody EventOrganaizer updatedData) {
		try {
			EventOrganaizer updated = eventOrganaizerService.updateEventOrganaizer(userId, eventOrganaizerId,
					updatedData);
			return new ResponseEntity<>(updated, HttpStatus.OK);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ArithmeticException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * ✅ Delete Event Organizer
	 */
	@DeleteMapping("/delete/{userId}/{eventOrganaizerId}")
	public ResponseEntity<?> deleteEventOrganaizer(@PathVariable String userId,
			@PathVariable String eventOrganaizerId) {
		try {
			boolean deleted = eventOrganaizerService.deleteEventOrganaizer(userId, eventOrganaizerId);
			if (deleted)
				return new ResponseEntity<>("Event Organizer deleted successfully.", HttpStatus.OK);
			else
				return new ResponseEntity<>("No changes occurred, delete failed.", HttpStatus.NOT_MODIFIED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
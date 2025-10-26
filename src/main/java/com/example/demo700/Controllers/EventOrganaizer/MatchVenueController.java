package com.example.demo700.Controllers.EventOrganaizer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.Models.EventOrganaizer.MatchVenue;
import com.example.demo700.Services.EventOrganaizer.MatchVenueService;

@RestController
@RequestMapping("/api/match-venue")
public class MatchVenueController {

    @Autowired
    private MatchVenueService matchVenueService;

    /**
     * ✅ Add a new MatchVenue
     * @param matchVenue - MatchVenue object
     * @param userId - Organizer User Id
     */
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addMatchVenue(@RequestBody MatchVenue matchVenue, @PathVariable String userId) {
        try {
            MatchVenue savedVenue = matchVenueService.addMatchVenue(matchVenue, userId);
            return new ResponseEntity<>(savedVenue, HttpStatus.CREATED);
        } catch (NullPointerException | NoSuchElementException | ArithmeticException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error occurred while adding MatchVenue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ✅ Get all MatchVenues
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllMatchVenues() {
        List<MatchVenue> venues = matchVenueService.seeAllMatchVenue();
        if (venues.isEmpty()) {
            return new ResponseEntity<>("No MatchVenue records found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    /**
     * ✅ Update existing MatchVenue
     * @param matchVenueId - Id of the matchVenue document
     * @param userId - Organizer User Id
     * @param matchVenue - Updated object
     */
    @PutMapping("/update/{matchVenueId}/{userId}")
    public ResponseEntity<?> updateMatchVenue(
            @PathVariable String matchVenueId,
            @PathVariable String userId,
            @RequestBody MatchVenue matchVenue) {
        try {
            MatchVenue updated = matchVenueService.updateMatchVenue(matchVenue, userId, matchVenueId);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error occurred while updating MatchVenue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ✅ Get MatchVenue by Venue Id
     */
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<?> findByVenueId(@PathVariable String venueId) {
        try {
            List<MatchVenue> venues = matchVenueService.findByVenueId(venueId);
            if (venues.isEmpty()) {
                return new ResponseEntity<>("No match venues found for venueId: " + venueId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(venues, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ✅ Get MatchVenue by Match Id
     */
    @GetMapping("/match/{matchId}")
    public ResponseEntity<?> findByMatchId(@PathVariable String matchId) {
        try {
            MatchVenue mv = matchVenueService.findByMatchId(matchId);
            if (mv == null) {
                return new ResponseEntity<>("No match venue found for matchId: " + matchId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(mv, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ✅ Delete MatchVenue
     * @param matchVenueId - Id of matchVenue
     * @param userId - Organizer Id
     */
    @DeleteMapping("/delete/{matchVenueId}/{userId}")
    public ResponseEntity<?> deleteMatchVenue(
            @PathVariable String matchVenueId,
            @PathVariable String userId) {
        try {
            boolean deleted = matchVenueService.deleteMatchVenue(matchVenueId, userId);
            if (deleted) {
                return new ResponseEntity<>("MatchVenue deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No changes detected or deletion failed", HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error occurred while deleting MatchVenue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


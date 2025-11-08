package com.example.demo700.Controllers.EventOrganaizer;

import java.time.Instant;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Services.EventOrganaizer.MatchService;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * ✅ Create a new match
     * @param userId Organizer user ID
     * @param match  Match details
     * @return Created match object
     */
    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createMatch(@PathVariable String userId, @RequestBody Match match) {
        try {
            Match savedMatch = matchService.createMatch(match, userId);
            return new ResponseEntity<>(savedMatch, HttpStatus.CREATED);
        } catch (NoSuchElementException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating match: " + e.getMessage());
        }
    }

    /**
     * ✅ Update existing match
     * @param userId Organizer user ID
     * @param matchId Match ID to update
     * @param match Updated match details
     * @return Updated match object
     */
    @PutMapping("/update/{userId}/{matchId}")
    public ResponseEntity<?> updateMatch(
            @PathVariable String userId,
            @PathVariable String matchId,
            @RequestBody Match match) {
        try {
            Match updated = matchService.updateMatch(match, userId, matchId);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating match: " + e.getMessage());
        }
    }

    /**
     * ✅ Delete a match
     * @param userId Organizer or Admin user ID
     * @param matchId Match ID
     * @return Success message
     */
    @DeleteMapping("/delete/{userId}/{matchId}")
    public ResponseEntity<?> deleteMatch(
            @PathVariable String userId,
            @PathVariable String matchId) {
        try {
            boolean deleted = matchService.deleteMatch(matchId, userId);
            if (deleted)
                return ResponseEntity.ok("Match deleted successfully.");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete match.");
        } catch (NoSuchElementException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting match: " + e.getMessage());
        }
    }

    /**
     * ✅ Get all matches
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllMatches() {
        try {
            List<Match> matches = matchService.seeAllMatch();
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving matches.");
        }
    }

    /**
     * ✅ Get matches by organizer ID
     */
    @GetMapping("/organizer/{organaizerId}")
    public ResponseEntity<?> getByOrganizer(@PathVariable String organaizerId) {
        try {
            List<Match> matches = matchService.findByOrganaizerId(organaizerId);
            return ResponseEntity.ok(matches);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * ✅ Get matches by team ID
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getByTeam(@PathVariable String teamId) {
        try {
            List<Match> matches = matchService.findByTeamsContainingIgnoreCase(teamId);
            return ResponseEntity.ok(matches);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * ✅ Get matches by game logs URL substring
     */
    @GetMapping("/gamelogs")
    public ResponseEntity<?> getByGameLogs(@RequestParam String keyword) {
        try {
            List<Match> matches = matchService.findByGameLogsContainingIgnoreCase(keyword);
            return ResponseEntity.ok(matches);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * ✅ Get matches by video URL substring
     */
    @GetMapping("/videos")
    public ResponseEntity<?> getByVideos(@RequestParam String keyword) {
        try {
            List<Match> matches = matchService.findByVideosContainingIgnoreCase(keyword);
            return ResponseEntity.ok(matches);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * ✅ Find match by time slot
     */
    @GetMapping("/time")
    public ResponseEntity<?> getByTime(
            @RequestParam Instant startTime,
            @RequestParam Instant endTime) {
        try {
            Match match = matchService.findByMatchStartTimeAndMatchEndTime(startTime, endTime);
            return ResponseEntity.ok(match);
        } catch (NoSuchElementException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid time input.");
        }
    }

    /**
     * ✅ Find matches with price greater than a given value
     */
    @GetMapping("/price/greater-than/{price}")
    public ResponseEntity<?> getByPriceGreaterThan(@PathVariable double price) {
        try {
            List<Match> matches = matchService.findByPriceGreaterThan(price);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching matches: " + e.getMessage());
        }
    }
}

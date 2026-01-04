package com.example.demo700.Controllers.TeamLocationControllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.TeamLocationModels.TeamLocationModel;
import com.example.demo700.Services.TeamLocationServices.TeamLocationService;

@RestController
@RequestMapping("/team-location")
@CrossOrigin
public class TeamLocationController {

    @Autowired
    private TeamLocationService teamLocationService;

    // ---------------- ADD ----------------
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addTeamLocation(
            @PathVariable String userId,
            @RequestBody TeamLocationModel teamLocation) {

        try {
            return new ResponseEntity<>(
                    teamLocationService.addTeamLocation(teamLocation, userId),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/update/{userId}/{teamLocationId}")
    public ResponseEntity<?> updateTeamLocation(
            @PathVariable String userId,
            @PathVariable String teamLocationId,
            @RequestBody TeamLocationModel teamLocation) {

        try {
            return new ResponseEntity<>(
                    teamLocationService.upadteTeamLocation(teamLocation, userId, teamLocationId),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- GET ALL ----------------
    @GetMapping("/all")
    public ResponseEntity<?> getAllTeamLocations() {
        try {
            return ResponseEntity.ok(teamLocationService.seeAllTeamLocation());
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getByTeamLocationId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(teamLocationService.getByTeamLocationId(id));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- GET BY TEAM ID ----------------
    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getByTeamId(@PathVariable String teamId) {
        try {
            return ResponseEntity.ok(teamLocationService.findByTeamId(teamId));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- SEARCH BY LOCATION NAME ----------------
    @GetMapping("/search/location-name")
    public ResponseEntity<?> searchByLocationName(@RequestParam String name) {
        try {
            return ResponseEntity.ok(
                    teamLocationService.findByLocationNameContainingIgnoreCase(name)
            );
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- SEARCH BY LAT & LONG ----------------
    @GetMapping("/search/lat-long")
    public ResponseEntity<?> searchByLatLong(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        try {
            return ResponseEntity.ok(
                    teamLocationService.findByLatitudeAndLongitude(latitude, longitude)
            );
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/delete/{userId}/{teamLocationId}")
    public ResponseEntity<?> deleteTeamLocation(
            @PathVariable String userId,
            @PathVariable String teamLocationId) {

        try {
            boolean deleted = teamLocationService.removeTeamLocation(userId, teamLocationId);
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ---------------- COMMON ERROR HANDLER ----------------
    private ResponseEntity<String> errorResponse(Exception e) {

        if (e instanceof NullPointerException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

        if (e instanceof NoSuchElementException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        if (e instanceof ArithmeticException) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + e.getMessage());
    }
}

package com.example.demo700.Controllers.AthleteLocationController;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Services.AthleteLocationService.AthleteLocationService;

@RestController
@RequestMapping("/api/athleteLocation")
public class AthleteLocationController {

    @Autowired
    private AthleteLocationService athleteLocationService;

    // ---------------------------------------------------
    // ADD ATHLETE LOCATION
    // ---------------------------------------------------
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addAthleteLocation(
            @PathVariable String userId,
            @RequestBody AthleteLocation athleteLocation) {

        try {
            AthleteLocation saved =
                    athleteLocationService.addAthleteLocation(athleteLocation, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ---------------------------------------------------
    // SEE ALL LOCATIONS
    // ---------------------------------------------------
    @GetMapping("/all")
    public ResponseEntity<?> seeAll() {
        try {
            List<AthleteLocation> list = athleteLocationService.seeAllAthleteLocation();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ---------------------------------------------------
    // FIND BY ATHLETE ID
    // ---------------------------------------------------
    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<?> findByAthleteId(@PathVariable String athleteId) {
        try {
            AthleteLocation location =
                    athleteLocationService.findByAthleteId(athleteId);
            return ResponseEntity.ok(location);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ---------------------------------------------------
    // FIND BY LOCATION NAME
    // ---------------------------------------------------
    @GetMapping("/location/{locationName}")
    public ResponseEntity<?> findByLocationName(@PathVariable String locationName) {
        try {
            return ResponseEntity.ok(
                    athleteLocationService.findByLocationName(locationName));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ---------------------------------------------------
    // FIND BY LATITUDE & LONGITUDE
    // ---------------------------------------------------
    @GetMapping("/coordinate")
    public ResponseEntity<?> findByLatLng(
            @RequestParam double lattitude,
            @RequestParam double longitude) {

        try {
            return ResponseEntity.ok(
                    athleteLocationService.findByLattitudeAndLongitude(lattitude, longitude));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ---------------------------------------------------
    // UPDATE ATHLETE LOCATION
    // ---------------------------------------------------
    @PutMapping("/update/{athleteLocationId}/{userId}")
    public ResponseEntity<?> updateAthleteLocation(
            @PathVariable String athleteLocationId,
            @PathVariable String userId,
            @RequestBody AthleteLocation athleteLocation) {

        try {
            AthleteLocation updated =
                    athleteLocationService.updateAthleteLocation(
                            athleteLocation, userId, athleteLocationId);

            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ---------------------------------------------------
    // DELETE ATHLETE LOCATION
    // ---------------------------------------------------
    @DeleteMapping("/delete/{athleteLocationId}/{userId}")
    public ResponseEntity<?> deleteAthleteLocation(
            @PathVariable String athleteLocationId,
            @PathVariable String userId) {

        try {
            boolean deleted =
                    athleteLocationService.deleteAthleteLocation(userId, athleteLocationId);

            return ResponseEntity.ok("Deleted = " + deleted);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

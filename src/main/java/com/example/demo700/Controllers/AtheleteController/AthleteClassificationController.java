package com.example.demo700.Controllers.AtheleteController;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Services.Athlete.AthleteClassificationService;

@RestController
@RequestMapping("/api/athlete-classification")
public class AthleteClassificationController {

    @Autowired
    private AthleteClassificationService athleteClassificationService;

    // ======================================
    // ADD CLASSIFICATION
    // ======================================
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addAthleteClassification(
            @RequestBody AthleteClassification athleteClassification,
            @PathVariable String userId) {

        try {
            AthleteClassification saved =
                    athleteClassificationService.addAthleteClassification(athleteClassification, userId);

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
    public ResponseEntity<?> updateAthleteClassification(
            @RequestBody AthleteClassification athleteClassification,
            @PathVariable String classificationId,
            @PathVariable String userId) {

        try {
            AthleteClassification updated =
                    athleteClassificationService.updateAthleteClassification(
                            athleteClassification, userId, classificationId);

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
    public ResponseEntity<?> seeAll() {

        try {
            List<AthleteClassification> list = athleteClassificationService.seeAll();
            return ResponseEntity.ok(list);

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
            AthleteClassification classification =
                    athleteClassificationService.findById(id);

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
            AthleteClassification classification =
                    athleteClassificationService.findByAthleteId(athleteId);

            return ResponseEntity.ok(classification);

        } catch (NullPointerException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ======================================
    // FIND BY CLASSIFICATION TYPE (ENUM)
    // ======================================
    @GetMapping("/type/{type}")
    public ResponseEntity<?> findByAthleteClassificationTypes(
            @PathVariable AthleteClassificationTypes type) {

        try {
            List<AthleteClassification> list =
                    athleteClassificationService.findByAthleteClassificationTypes(type);

            return ResponseEntity.ok(list);

        } catch (NullPointerException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ======================================
    // DELETE CLASSIFICATION
    // ======================================
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> removeAthleteClassification(
            @PathVariable String id,
            @PathVariable String userId) {

        try {
            boolean deleted =
                    athleteClassificationService.removeAthleteClassification(id, userId);

            return ResponseEntity.ok("Deleted : " + deleted);

        } catch (NullPointerException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred while deleting athlete classification");
        }
    }
}

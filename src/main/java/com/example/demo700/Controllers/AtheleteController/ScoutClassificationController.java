package com.example.demo700.Controllers.AtheleteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.DTOFiles.ScoutClassificationListResonseDTO;
import com.example.demo700.DTOFiles.ScoutResponse;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.ScoutClassification;
import com.example.demo700.Services.Athlete.ScoutClassificationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/scout-classification")
public class ScoutClassificationController {

    @Autowired
    private ScoutClassificationService scoutClassificationService;

    // ==================== CREATE ====================
    @PostMapping
    public ResponseEntity<?> addClassification(
            @RequestBody ScoutClassification scoutClassification,
            @RequestParam String userId) {

        try {
            ScoutClassification response =
                    scoutClassificationService.addAthleteClassification(scoutClassification, userId);

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
            @RequestBody ScoutClassification scoutClassification,
            @RequestParam String userId) {

        try {
            ScoutClassification response =
                    scoutClassificationService.updateAthleteClassification(scoutClassification, userId, id);

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

            ScoutClassificationListResonseDTO response =
                    scoutClassificationService.seeAll(page, size, requestUrl);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ==================== GET BY ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {

        try {
            ScoutResponse response = scoutClassificationService.findById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ==================== GET BY SCOUT ID ====================
    @GetMapping("/scout/{scoutId}")
    public ResponseEntity<?> getByScoutId(@PathVariable String scoutId) {

        try {
            ScoutResponse response = scoutClassificationService.findByCoachId(scoutId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ==================== FILTER BY TYPE ====================
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getByType(
            @PathVariable AthleteClassificationTypes type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        try {
            String requestUrl = request.getRequestURL().toString()
                    + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

            ScoutClassificationListResonseDTO response =
                    scoutClassificationService.findByAthleteClassificationTypes(type, page, size, requestUrl);

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
                    scoutClassificationService.removeAthleteClassification(id, userId);

            if (deleted) {
                return new ResponseEntity<>("Scout classification deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to delete scout classification", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
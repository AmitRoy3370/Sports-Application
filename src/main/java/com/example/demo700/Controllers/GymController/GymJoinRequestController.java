package com.example.demo700.Controllers.GymController;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.GymModels.GymJoinRequest;
import com.example.demo700.Models.GymModels.GymMember;
import com.example.demo700.Services.GymServices.GymJoinRequestService;

@RestController
@RequestMapping("/api/gym-join-request")
public class GymJoinRequestController {

    @Autowired
    private GymJoinRequestService gymJoinRequestService;

    // ✅ ADD JOIN REQUEST
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody GymJoinRequest gymJoinRequest) {
        try {
            GymJoinRequest saved = gymJoinRequestService.addGymJoinRequest(gymJoinRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ UPDATE JOIN REQUEST
    @PutMapping("/update/{requestId}/{userId}/{gymId}")
    public ResponseEntity<?> update(
            @RequestBody GymJoinRequest gymJoinRequest,
            @PathVariable String requestId,
            @PathVariable String userId,
            @PathVariable String gymId) {
        try {
            GymJoinRequest updated =
                    gymJoinRequestService.updayeGymJoinRequest(gymJoinRequest, userId, requestId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(gymJoinRequestService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ SEE ALL
    @GetMapping("/all")
    public ResponseEntity<?> seeAll() {
        try {
            return ResponseEntity.ok(gymJoinRequestService.seeAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ FIND BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable String userId) {
        try {
            List<GymJoinRequest> list = gymJoinRequestService.findByUserId(userId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ FIND BY GYM ID
    @GetMapping("/gym/{gymId}")
    public ResponseEntity<?> findByGymId(@PathVariable String gymId) {
        try {
            return ResponseEntity.ok(gymJoinRequestService.findByGymId(gymId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ FIND BY TIME BEFORE
    @GetMapping("/time/before")
    public ResponseEntity<?> findBefore(@RequestParam("time") String time) {
        try {
            Instant instant = Instant.parse(time);
            return ResponseEntity.ok(
                    gymJoinRequestService.findByRequestSendingTimeBefore(instant));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ FIND BY TIME AFTER
    @GetMapping("/time/after")
    public ResponseEntity<?> findAfter(@RequestParam("time") String time) {
        try {
            Instant instant = Instant.parse(time);
            return ResponseEntity.ok(
                    gymJoinRequestService.findByRequestSendingTimeAfter(instant));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ DELETE JOIN REQUEST
    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<?> remove(
            @PathVariable String id,
            @PathVariable String userId) {
        try {
            boolean removed = gymJoinRequestService.removeGymJoinRequest(id, userId);
            return ResponseEntity.ok(removed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ HANDLE JOIN REQUEST (ACCEPT / REJECT)
    @PostMapping("/handle/{id}/{userId}")
    public ResponseEntity<?> handleRequest(
            @PathVariable String id,
            @PathVariable String userId,
            @RequestParam boolean response) {
        try {
            GymMember result =
                    gymJoinRequestService.handleGymJoinRequest(id, userId, response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ FIND BY GYM ID + USER ID
    @GetMapping("/gym/{gymId}/user/{userId}")
    public ResponseEntity<?> findByGymAndUser(
            @PathVariable String gymId,
            @PathVariable String userId) {
        try {
            return ResponseEntity.ok(
                    gymJoinRequestService.findByGymIdAndUserId(gymId, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


package com.example.demo700.Controllers.GymController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.GymModels.GymMember;
import com.example.demo700.Services.GymServices.GymMemberService;

@RestController
@RequestMapping("/api/gym-members")
public class GymMemberController {

    @Autowired
    private GymMemberService gymMemberService;

    // ✅ ADD MEMBER TO GYM
    @PostMapping("/add")
    public ResponseEntity<?> addGymMember(
            @RequestParam String memberId,
            @RequestParam String gymId,
            @RequestParam String userId) {
        try {
            GymMember member =
                    gymMemberService.addGymMember(memberId, gymId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ REMOVE MEMBER FROM GYM (BY MEMBER ID + GYM ID)
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeGymMember(
            @RequestParam String memberId,
            @RequestParam String gymId,
            @RequestParam String userId) {
        try {
            GymMember member =
                    gymMemberService.removeGymMember(memberId, gymId, userId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ FIND BY GYM ID
    @GetMapping("/gym/{gymId}")
    public ResponseEntity<?> findByGymId(@PathVariable String gymId) {
        try {
            GymMember member = gymMemberService.findByGymId(gymId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ FIND BY MEMBER ID (GYM MEMBER DOCUMENT ID)
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try {
            GymMember member = gymMemberService.findById(id);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ FIND BY MEMBER USER ID (CONTAINING IGNORE CASE)
    @GetMapping("/search")
    public ResponseEntity<?> findByGymMembersContaining(
            @RequestParam String gymMembers) {
        try {
            List<GymMember> list =
                    gymMemberService.findByGymMembersContaingingIgnoreCase(gymMembers);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ SEE ALL GYM MEMBERS
    @GetMapping("/all")
    public ResponseEntity<?> seeAll() {
        try {
            List<GymMember> list = gymMemberService.seeAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ DELETE GYM MEMBER DOCUMENT (ADMIN / OWNER)
    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<?> deleteGymMember(
            @PathVariable String id,
            @PathVariable String userId) {
        try {
            boolean deleted = gymMemberService.removeGymMember(id, userId);
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


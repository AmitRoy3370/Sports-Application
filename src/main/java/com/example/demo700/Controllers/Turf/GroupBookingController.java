package com.example.demo700.Controllers.Turf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.Models.Turf.GroupBooking;
import com.example.demo700.Repositories.Turf.GroupBookingRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/group-bookings")
@RequiredArgsConstructor
public class GroupBookingController {

	@Autowired
    private GroupBookingRepository groupBookingRepository;

    @GetMapping
    public ResponseEntity<List<GroupBooking>> all() {
        return ResponseEntity.ok(groupBookingRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupBooking> getById(@PathVariable String id) {
        return groupBookingRepository.findById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/add-member")
    public ResponseEntity<GroupBooking> addMember(@PathVariable String id, @RequestParam String userId) {
        GroupBooking gb = groupBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        gb.getMemberIds().add(userId);
        groupBookingRepository.save(gb);
        return ResponseEntity.ok(gb);
    }
}

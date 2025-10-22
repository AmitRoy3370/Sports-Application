package com.example.demo700.Controllers.Turf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.Models.Turf.Owner;
import com.example.demo700.Services.TurfServices.TurfOwnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/turfOwner")
@RequiredArgsConstructor
public class OwnerController {

	@Autowired
	private TurfOwnerService turfOwnerService;

	@PostMapping("/addOwner")
	public ResponseEntity<?> addTurfOwner(@RequestBody Owner owner) {

		owner = turfOwnerService.addOwner(owner);

		if (owner == null) {

			return ResponseEntity.status(400).body("Data is not added");

		}

		return ResponseEntity.status(201).body(owner);

	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAll() {

		List<Owner> list = turfOwnerService.seeAllOwner();

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("No owner find at here");

		}

		return ResponseEntity.status(200).body(list);

	}

	@PutMapping("/updateOwner")
	public ResponseEntity<?> updateOwner(@RequestParam String id, @RequestBody Owner owner) {

		try {

			owner = turfOwnerService.updateOwnerData(id, owner);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(200).body(owner);

	}

	@DeleteMapping("/remove")
	public ResponseEntity<?> removeOwner(@RequestParam String id, @RequestParam String ownId) {

		boolean yes = turfOwnerService.removeOwner(id, ownId);

		if (yes) {

			return ResponseEntity.status(200).body("Data is deleted...");

		} else {

			return ResponseEntity.status(400).body("Data is not deleted...");

		}

	}

}

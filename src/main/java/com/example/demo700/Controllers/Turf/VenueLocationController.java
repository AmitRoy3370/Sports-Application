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

import com.example.demo700.Models.Turf.VenueLocation;
import com.example.demo700.Services.TurfServices.VenueLocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venueLocation")
@RequiredArgsConstructor
public class VenueLocationController {
	
	@Autowired
	private VenueLocationService venueLocationService;
	
	@PostMapping("/addVenue")
	public ResponseEntity<?> addVenueLocation(@RequestBody VenueLocation venueLocation, @RequestParam String userId) {
		
		try {
			
			System.out.println("userId :- " + userId);
			System.out.println("located venue id :- " + venueLocation.getVenueId());
			
			venueLocation = venueLocationService.addVenueLocation(venueLocation, userId);
			
			return ResponseEntity.status(201).body(venueLocation);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllVenueLocation() {
		
		try {
			
			List<VenueLocation> list = venueLocationService.seeAllVenueLocation();
			
			return ResponseEntity.status(200).body(list);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/seeVenueLocationBySearch")
	public ResponseEntity<?> searchVenueLocation(@RequestParam String venueLocation) {
		
		try {
			
			List<VenueLocation> list = venueLocationService.searchVenueLocationByName(venueLocation);
			
			return ResponseEntity.status(200).body(list);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/seeVenueLocation")
	public ResponseEntity<?> searchVenueLocationById(@RequestParam String venueLocationId) {
		
		try {
			
			VenueLocation list = venueLocationService.searchVenueLocation(venueLocationId);
			
			return ResponseEntity.status(200).body(list);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateVenueLocation(@RequestParam String venueLocationId, @RequestBody VenueLocation updatedVenueLocation, @RequestParam String userId) {
		
		try {
			
			VenueLocation venueLocation = venueLocationService.updateVenueLocation(venueLocationId, updatedVenueLocation, userId);
			
			return ResponseEntity.status(200).body(venueLocation);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteVenue(@RequestParam String venueLocationId, @RequestParam String userId) {
	
		try {
			
			boolean yes = venueLocationService.deleteVenueLocation(venueLocationId, userId);
			
			if(yes) {
				
				return ResponseEntity.status(200).body("Data is deleted...");
				
			} else {
				
				return ResponseEntity.status(500).body("Data is not deleted...");
				
			}
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}

}

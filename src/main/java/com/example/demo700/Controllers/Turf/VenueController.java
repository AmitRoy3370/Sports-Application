package com.example.demo700.Controllers.Turf;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Services.TurfServices.VenueService;
import com.example.demo700.Validator.AddressValidator;
import com.example.demo700.Validator.URLValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

	@Autowired
	private VenueService venueService;

	URLValidator urlValidator = new URLValidator();
	AddressValidator adressValidator = new AddressValidator();

	@PostMapping("/addVenue")
	public ResponseEntity<?> createVenue(@Validated @RequestBody Venue v) {

		if (v == null || v.getAddress().isEmpty() || v.getName().isEmpty() || v.getOwnerId().isEmpty()
				|| v.getBasePricePerHour() <= 0.00 || v.getAmenities().isEmpty() || v.getPhotos().isEmpty()
				|| v.getSportsSupported().isEmpty()) {

			return ResponseEntity.status(400).body("Have to put all the required arguments at here");

		} else if (!urlValidator.isValid(v.getPhotos())) {

			return ResponseEntity.status(400).body("Have to put all the urls perfectly at here");

		} else if (!adressValidator.isValidAddress(v.getAddress())) {

			return ResponseEntity.status(400).body("Have to put all the perfect address at here");

		}

		Venue list = (venueService.createVenue(v));

		if (list == null) {

			return ResponseEntity.status(500).body("Data is not added...");

		} else {

			return ResponseEntity.ok(list);

		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Venue> getById(@PathVariable String id) {
		return venueService.getVenueById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam String q) {

		List<Venue> list = venueService.searchByAddress(q);

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("No Venue in this place");

		} else {

			return ResponseEntity.ok(list);

		}
	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAll() {

		List<Venue> list = venueService.getAllVenue();

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("No Venue find at here...");

		} else {

			return ResponseEntity.status(200).body(list);

		}

	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateVenue(@RequestParam String id, @RequestBody Venue venue) {
	
		venue = venueService.updateVeue(id, venue);
		
		if(venue == null) {
			
			return ResponseEntity.status(400).body("Veneue not updated...");
			
		}
		
		return ResponseEntity.status(200).body(venue);
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteVenue(@RequestParam String id, @RequestParam String userId) {
		
		boolean yes = venueService.removeVenue(id, userId);
		
		if(!yes) {
			
			return ResponseEntity.status(400).body("Data is not deleted...");
			
		}
		
		return ResponseEntity.status(200).body("Data is deleted successfully....");
		
	}
	
}

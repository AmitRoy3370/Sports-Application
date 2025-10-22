package com.example.demo700.Controllers.AtheleteController;

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

import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Services.Athlete.CoachService;

@RestController
@RequestMapping("/api/coach")
public class CoachController {
	
	@Autowired
	private CoachService coachService;
	
	@PostMapping("/addCoach")
	public ResponseEntity<?> addCoach(@RequestBody Coach coach, @RequestParam String userId) {
		
		try {
			
			coach = coachService.addCoach(coach, userId);
			
			if(coach == null) {
				
				return ResponseEntity.status(500).body("Coach is not added...");
				
			}
			
		} catch(Exception e) {
			
			return ResponseEntity.status(201).body(e.getMessage());
			
		}
		
		return ResponseEntity.status(201).body(coach);
		
	}
	
	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllCoach() {
		
		List<Coach> list = coachService.seeAll();
		
		if(list == null) {
			
			return ResponseEntity.status(404).body("No Coach find at here...");
			
		}
		
		return ResponseEntity.status(200).body(list);
		
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> searchCoach(@RequestParam String coachId) {
		
		Coach coach = coachService.searchCoach(coachId);
		
		if(coach == null) {
			
			return ResponseEntity.status(404).body("No coach find at here...");
			
		}
		
		return ResponseEntity.status(200).body(coach);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateCoach(@RequestBody Coach coach, @RequestParam String userId, @RequestParam String coachId) {
		
		try {
			
			coach = coachService.updateCoach(coach, userId, coachId);
			
			if(coach == null) {
				
				return ResponseEntity.status(500).body("Coach is not updated at here..");
				
			}
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
		return ResponseEntity.status(200).body(coach);
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCoach(@RequestParam String coachId, @RequestParam String userId) {
		
		try {
			
			boolean yes = coachService.deleteCoach(coachId, userId);
			
			if(!yes) {
				
				return ResponseEntity.status(500).body("Coach is not deleted...");
				
			}
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
		return ResponseEntity.status(200).body("Coach deleted...");
		
	}

}

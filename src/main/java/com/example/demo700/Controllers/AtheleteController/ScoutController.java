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

import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Services.Athlete.ScoutService;

@RestController
@RequestMapping("/api/scout")
public class ScoutController {

	@Autowired
	private ScoutService scoutService;
	
	@PostMapping("/addScout")
	public ResponseEntity<?> addScouts(@RequestBody Scouts scout, @RequestParam String userId) {
		
		try {
			
			scout = scoutService.addScout(scout, userId);
			
			if(scout == null) {
				
				return ResponseEntity.status(500).body("Scout is not added at here...");
				
			}
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
		return ResponseEntity.status(201).body(scout);
		
	}
	
	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllScouts() {
		
		List<Scouts> list = scoutService.seeAllScouts();
		
		if(list == null) {
			
			return ResponseEntity.status(404).body("No scout's find at here...");
			
		}
		
		return ResponseEntity.status(200).body(list);
		
	}
	
	@GetMapping("/searchByAtheleteId")
	public ResponseEntity<?> findByAtheleteId(@RequestParam String atheleteId) {
		
		try {
			
			Scouts scouts = scoutService.findByAtheleteId(atheleteId);
			
			if(scouts == null) {
				
				return ResponseEntity.status(404).body("No such scout's find at here..");
				
			}
			
			return ResponseEntity.status(200).body(scouts);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	// ✅ Update existing scout
    @PutMapping("/updateScout")
    public ResponseEntity<?> updateScouts(@RequestParam String scoutId,
                                          @RequestParam String userId,
                                          @RequestBody Scouts scout) {
        try {
            Scouts updated = scoutService.updateScouts(scoutId, userId, scout);
            if (updated == null) {
                return ResponseEntity.status(500).body("Scout is not updated properly...");
            }
            return ResponseEntity.status(200).body(updated);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // ✅ Delete a scout
    @DeleteMapping("/deleteScout")
    public ResponseEntity<?> deleteScouts(@RequestParam String scoutId, @RequestParam String userId) {
        try {
            boolean deleted = scoutService.deleteScouts(scoutId, userId);
            if (!deleted) {
                return ResponseEntity.status(404).body("No such scout found or deletion failed...");
            }
            return ResponseEntity.status(200).body("Scout deleted successfully...");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // ✅ Find scouts by event participation
    @GetMapping("/searchByEvent")
    public ResponseEntity<?> findByEvent(@RequestParam String eventId) {
        try {
            List<Scouts> list = scoutService.findByEventsContainingIgnoreCase(eventId);
            if (list == null || list.isEmpty()) {
                return ResponseEntity.status(404).body("No scouts found for this event...");
            }
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // ✅ Find scouts by match participation
    @GetMapping("/searchByMatch")
    public ResponseEntity<?> findByMatch(@RequestParam String matchId) {
        try {
            List<Scouts> list = scoutService.findByMatchesContainingIgnoreCase(matchId);
            if (list == null || list.isEmpty()) {
                return ResponseEntity.status(404).body("No scouts found for this match...");
            }
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
	
}

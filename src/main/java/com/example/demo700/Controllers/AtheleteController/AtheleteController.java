package com.example.demo700.Controllers.AtheleteController;

import java.util.List;
import java.util.Optional;

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

import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Services.Athlete.AtheleteService;

@RestController
@RequestMapping("/api/athelete")
public class AtheleteController {

	@Autowired
	private AtheleteService atheleteService;

	@PostMapping("/addAthelete")
	public ResponseEntity<?> addAthelete(@RequestBody Athelete athelete, @RequestParam String userId) {

		try {

			athelete = atheleteService.addAthelete(athelete, userId);

			if (athelete == null) {

				return ResponseEntity.status(500).body("The athelete is not added...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(201).body(athelete);

	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllAthelete() {

		List<Athelete> list = atheleteService.seeAll();

		if (list == null) {

			return ResponseEntity.status(404).body("No athelete find at here...");

		}

		return ResponseEntity.status(200).body(list);

	}

	@GetMapping("/searchByAge")
	public ResponseEntity<?> searchAteleteByAge(@RequestParam String age) {

		try {
			int _age = Integer.parseInt(age);

			List<Athelete> list = atheleteService.findByAgeLessThan(_age);

			if (list == null) {

				return ResponseEntity.status(404).body("No athelete find at here..");
			} else {

				return ResponseEntity.status(200).body(list);

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body("Please enter a valid age...");

		}

	}
	
	@GetMapping("/searchByHeight")
	public ResponseEntity<?> searchAtheleteByHeight(@RequestParam String height) {
		
		try {
			
			double _height = Double.parseDouble(height);
			
			List<Athelete> list = atheleteService.findByHeightGreaterThan(_height);
			
			return ResponseEntity.status(200).body(list);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body("Have to input the valid height at here..");
			
		}
		
	}
	
	@GetMapping("/findByTeamName")
	public ResponseEntity<?> findByTeamName(@RequestParam String teamName) {
		
		try {
			
			List<Athelete> list = atheleteService.findByPresentTeamIgnoreCase(teamName);
			
			if(list ==  null) {
				
				return ResponseEntity.status(404).body("No athelete find at here..");
				
			}
			
			return ResponseEntity.status(200).body(list);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/findByWeightRange")
	public ResponseEntity<?> findByWeightRange(@RequestParam String minHeight, @RequestParam String maxHeight) {
		
		try {
			
			int _minHeight = Integer.parseInt(minHeight);
			int _maxHeight = Integer.parseInt(maxHeight);
			
			List<Athelete> list = atheleteService.findByWeightRange(_minHeight, _maxHeight);
			
			if(list == null) {
				
				return ResponseEntity.status(404).body("No athelete find at here...");
				
			}
			
			return ResponseEntity.status(200).body(list);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/findByAgeLessThanAndHeightGreaterThan")
	public ResponseEntity<?> findByAgeLessThanAndHeightGreaterThan(@RequestParam String height, @RequestParam String age) {
		
		try {
			
			double _height = Double.parseDouble(height);
			int _age = Integer.parseInt(age);
			
			List<Athelete> list = atheleteService.findByAgeLessThanAndHeightGreaterThan(_age, _height);
			
			return ResponseEntity.status(200).body(list);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body("Have to input the valid height at here..");
			
		}
		
	}
	
	@GetMapping("/findByPosition")
	public ResponseEntity<?> findByPosition(@RequestParam String position) {
		
		try {
			
			int _position = Integer.parseInt(position);
			
			List<Athelete> list = atheleteService.findByPosition(_position);
			
			if(list == null) {
				
				return ResponseEntity.status(404).body("no athelete find at here..");
				
			} else {
				
				return ResponseEntity.status(200).body(list);
				
			}
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
			
		}
		
	}
	
	 // --------------------- FIND BY EVENT ATTENDENCE ---------------------
    @GetMapping("/findByEventAttendence")
    public ResponseEntity<?> findByEventAttendence(@RequestParam String eventName) {
        try {
            List<Athelete> list = atheleteService.findByEventAttendenceContainingIgnoreCase(eventName);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found for this event...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // --------------------- FIND BY GAME LOG ---------------------
    @GetMapping("/findByGameLog")
    public ResponseEntity<?> findByGameLog(@RequestParam String log) {
        try {
            List<Athelete> list = atheleteService.findByGameLogsContainingIgnoreCase(log);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found for this game log...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // --------------------- PARTIAL TEAM NAME SEARCH ---------------------
    @GetMapping("/searchByPartialTeamName")
    public ResponseEntity<?> searchByPartialTeamName(@RequestParam String partialName) {
        try {
            List<Athelete> list = atheleteService.searchByTeamNamePartial(partialName);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found matching team name...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // --------------------- FIND BY MULTIPLE EVENTS ---------------------
    @PostMapping("/findByMultipleEvents")
    public ResponseEntity<?> findByMultipleEvents(@RequestBody List<String> eventNames) {
        try {
            List<Athelete> list = atheleteService.findByMultipleEvents(eventNames);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found attending all these events...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // --------------------- FIND BY USER ID ---------------------
    @GetMapping("/findByUserId")
    public ResponseEntity<?> findByUserId(@RequestParam String userId) {
        try {
            Optional<Athelete> athlete = atheleteService.findByUserId(userId);
            if (!athlete.isPresent())
                return ResponseEntity.status(404).body("No athlete found with this userId...");
            return ResponseEntity.status(200).body(athlete);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // --------------------- DELETE BY USER ID ---------------------
    @DeleteMapping("/deleteByUserId")
    public ResponseEntity<?> deleteByUserId(@RequestParam String userId, @RequestParam String atheleteId) {
        try {
            boolean deletedCount = atheleteService.deleteByUserId(atheleteId, userId);
            if (!deletedCount)
                return ResponseEntity.status(404).body("No athlete found with this userId...");
            return ResponseEntity.status(200).body("Athlete deleted successfully...");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // --------------------- FIND BY WEIGHT LESS THAN ---------------------
    @GetMapping("/findByWeightLessThan")
    public ResponseEntity<?> findByWeightLessThan(@RequestParam String weight) {
        try {
            double _weight = Double.parseDouble(weight);
            List<Athelete> list = atheleteService.findByWeightLessThan(_weight);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found under this weight...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // --------------------- UPDATE ATHLETE DATA ---------------------
    @PutMapping("/updateAthelete")
    public ResponseEntity<?> updateAthelete(@RequestParam String id, @RequestBody Athelete updatedAthelete, @RequestParam String userId) {
        try {
            Athelete result = atheleteService.updateAthelete(updatedAthelete, userId, id);
            if (result == null)
                return ResponseEntity.status(404).body("Athlete not found for update...");
            return ResponseEntity.status(200).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
	

}

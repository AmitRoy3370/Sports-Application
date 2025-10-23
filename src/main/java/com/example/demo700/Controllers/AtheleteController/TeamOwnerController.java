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

import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Services.Athlete.TeamOwnerService;

@RestController
@RequestMapping("/api/teamOwner")
public class TeamOwnerController {

	@Autowired
	private TeamOwnerService temOwnerService;

	@PostMapping("/addTeamOwner")
	public ResponseEntity<?> addTeamOwner(@RequestBody TeamOwner teamOwner) {

		try {

			teamOwner = temOwnerService.addTeamOwner(teamOwner);

			if (teamOwner == null) {

				return ResponseEntity.status(500).body("Team owner is not added....");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(201).body(teamOwner);

	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllTeamOwner() {

		List<TeamOwner> list = temOwnerService.seeAllTeamOwner();

		if (list == null) {

			return ResponseEntity.status(404).body("No such team owner find at here...");

		}

		return ResponseEntity.status(200).body(list);

	}

	@GetMapping("/findByAchivements")
	public ResponseEntity<?> findByAchivementsContainingIgnoreCase(@RequestParam String achivement) {

		try {

			List<TeamOwner> list = temOwnerService.findByAchivementsContainingIgnoreCase(achivement);

			if (list == null) {

				return ResponseEntity.status(404).body("No team owner find at here...");

			}

			return ResponseEntity.status(200).body(list);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	@GetMapping("/findByMatches")
	public ResponseEntity<?> findByMatchesContainingIgnoreCase(@RequestParam String matchId) {

		try {

			List<TeamOwner> list = temOwnerService.findByMatchesContainingIgnoreCase(matchId);

			if (list == null) {

				return ResponseEntity.status(404).body("No team owner find at here...");

			}

			return ResponseEntity.status(200).body(list);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateTeamOwner(@RequestBody TeamOwner teamOwner, @RequestParam String userId) {

		try {

			teamOwner = temOwnerService.updateTeamOwner(teamOwner, userId);

			if (teamOwner == null) {

				return ResponseEntity.status(500).body("No team owner is updated...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(200).body(teamOwner);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteTeamOwner(@RequestParam String teamOwnerId, @RequestParam String userId) {

		try {

			boolean yes = temOwnerService.deleteTeamOwner(teamOwnerId, userId);

			if (yes) {

				return ResponseEntity.status(200).body("Team owner deleted...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(404).body("No Team owner delete at here...");

	}

	@GetMapping("/findByTeam")
	public ResponseEntity<?> findByTeam(@RequestParam String teamId) {

		if (teamId == null) {

			return ResponseEntity.status(400).body("False request...");

		}
		
		try {
			
			TeamOwner teamOwner = temOwnerService.findByTeamsContainingIgnoreCase(teamId);
			
			if(teamOwner == null) {
				
				throw new Exception();
				
			}
			
			return ResponseEntity.status(200).body(teamOwner);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(404).body("No team owner find at here...");
			
		}

	}

}

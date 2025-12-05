package com.example.demo700.Controllers.AtheleteController;

import java.util.List;
import java.util.NoSuchElementException;

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

import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Services.Athlete.TeamService;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    // -----------------------------
    // ✅ 1. Add new team
    // -----------------------------
    @PostMapping("/addTeam")
    public ResponseEntity<?> addTeam(@RequestBody Team team, @RequestParam String userId) {
        try {
            Team _team = teamService.addTeam(team, userId);
            if (_team == null) {
                return ResponseEntity.status(500).body("Team not added...");
            }
            return ResponseEntity.status(201).body(_team);
        } catch (NullPointerException e) {
            return ResponseEntity.status(400).body("Invalid request...");
        } catch (ArithmeticException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 2. See all teams
    // -----------------------------
    @GetMapping("/seeAll")
    public ResponseEntity<?> seeAllTeams() {
        try {
            List<Team> list = teamService.seeAllTeam();
            if (list == null || list.isEmpty()) {
                return ResponseEntity.status(404).body("No teams found...");
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 3. Update a team
    // -----------------------------
    @PutMapping("/updateTeam")
    public ResponseEntity<?> updateTeam(@RequestBody Team team, 
                                        @RequestParam String userId, 
                                        @RequestParam String teamId) {
        try {
            Team updatedTeam = teamService.updateTeam(team, userId, teamId);
            if (updatedTeam == null) {
                return ResponseEntity.status(500).body("Team not updated...");
            }
            return ResponseEntity.ok(updatedTeam);
        } catch (NullPointerException e) {
            return ResponseEntity.status(400).body("Invalid request...");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ArithmeticException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 4. Delete a team
    // -----------------------------
    @DeleteMapping("/deleteTeam")
    public ResponseEntity<?> deleteTeam(@RequestParam String teamId, @RequestParam String userId) {
        try {
            boolean deleted = teamService.deleteTeam(teamId, userId);
            if (deleted) {
                return ResponseEntity.ok("Team deleted successfully.");
            } else {
                return ResponseEntity.status(404).body("Team not found or deletion failed.");
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(400).body("Invalid request...");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ArithmeticException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 5. Find by athlete in team
    // -----------------------------
    @GetMapping("/findByAtheleteId")
    public ResponseEntity<?> findByAtheleteId(@RequestParam String atheleteId) {
        try {
            Team team = teamService.findByAtheletesContainingIgnoreCase(atheleteId);
            if (team == null) {
                return ResponseEntity.status(404).body("No team found for this athlete...");
            }
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 6. Find by coach in team
    // -----------------------------
    @GetMapping("/findByCoachId")
    public ResponseEntity<?> findByCoachId(@RequestParam String coachId) {
        try {
            Team team = teamService.findByCoachesContainingIgnoreCase(coachId);
            if (team == null) {
                return ResponseEntity.status(404).body("No team found for this coach...");
            }
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 7. Find by scout in team
    // -----------------------------
    @GetMapping("/findByScoutId")
    public ResponseEntity<?> findByScoutId(@RequestParam String scoutId) {
        try {
            Team team = teamService.findByScoutsContainingIgnoreCase(scoutId);
            if (team == null) {
                return ResponseEntity.status(404).body("No team found for this scout...");
            }
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 8. Find by match in team
    // -----------------------------
    @GetMapping("/findByMatchId")
    public ResponseEntity<?> findByMatchId(@RequestParam String matchId) {
        try {
            List<Team> team = teamService.findByMatchesContainingIgnoreCase(matchId);
            if (team.isEmpty()) {
                return ResponseEntity.status(404).body("No team found for this match...");
            }
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // -----------------------------
    // ✅ 9. Find by team owner
    // -----------------------------
    @GetMapping("/findByTeamOwnerId")
    public ResponseEntity<?> findByTeamOwnerId(@RequestParam String teamOwnerId) {
        try {
            List<Team> teams = teamService.findByTeamOwnerId(teamOwnerId);
            if (teams == null || teams.isEmpty()) {
                return ResponseEntity.status(404).body("No team found for this team owner...");
            }
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @GetMapping("/findByDoctorId")
    public ResponseEntity<?> findByDoctorId(@RequestParam String doctorId) {
    	
    	try {
    		
    		Team team = teamService.findByDoctorsContainingIgnoreCase(doctorId);
    		
    		if(team == null) {
    			
    			return ResponseEntity.status(404).body("No team find at here...");
    			
    		}
    		
    		return ResponseEntity.status(200).body(team);
    		
    	} catch(Exception e) {
    		
    		return ResponseEntity.status(400).body(e.getMessage());
    		
    	}
    	
    }
    
    @GetMapping("/findByTeamName")
    public ResponseEntity<?> findByTeamName(@RequestParam String teamName) {
    	
    	try {
    		
    		Team team = teamService.findByTeamName(teamName);
    		
    		if(team == null) {
    			
    			return ResponseEntity.status(404).body("No such team exist at here...");
    			
    		}
    		
    		return ResponseEntity.status(200).body(team);
    		
    	} catch(Exception e) {
    		
    		return ResponseEntity.status(400).body(e.getMessage());
    		
    	}
    	
    }
    
}
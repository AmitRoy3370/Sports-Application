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

import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Services.Athlete.AtheleteService;

@RestController
@RequestMapping("/api/athelete")
public class AtheleteController {

    @Autowired
    private AtheleteService atheleteService;
    
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 50;

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

    // 🔥 BACKWARD COMPATIBLE: Old URL still works with warning
    @GetMapping("/seeAll")
    public ResponseEntity<?> seeAllAthelete(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        try {
            // If pagination params are provided, use paginated version
            if (page != null || size != null) {
                int pageNum = page != null ? Math.max(0, page) : DEFAULT_PAGE;
                int pageSize = size != null ? Math.min(MAX_SIZE, Math.max(1, size)) : DEFAULT_SIZE;
                
                AthleteListResponseDTO response = atheleteService.seeAll(pageNum, pageSize);
                if (response.getAthletes().isEmpty()) {
                    return ResponseEntity.status(404).body("No athelete find at here...");
                }
                return ResponseEntity.status(200).body(response);
            } 
            // Otherwise use old method (with warning)
            else {
                List<AthleteRequestDTO> list = atheleteService.seeAll();
                if (list == null || list.isEmpty()) {
                    return ResponseEntity.status(404).body("No athelete find at here...");
                }
                return ResponseEntity.status(200).body(list);
            }
        } catch (UnsupportedOperationException e) {
            // If old method is disabled, suggest using pagination
            return ResponseEntity.status(400).body("This endpoint now requires pagination. Please use: /api/athelete/seeAll?page=0&size=20");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // 🔥 BACKWARD COMPATIBLE: Old URL still works
    @GetMapping("/searchByAge")
    public ResponseEntity<?> searchAteleteByAge(
            @RequestParam String age,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        try {
            int _age = Integer.parseInt(age);
            
            // If pagination params provided
            if (page != null || size != null) {
                int pageNum = page != null ? Math.max(0, page) : DEFAULT_PAGE;
                int pageSize = size != null ? Math.min(MAX_SIZE, Math.max(1, size)) : DEFAULT_SIZE;
                
                AthleteListResponseDTO response = atheleteService.findByAgeLessThan(_age, pageNum, pageSize);
                if (response.getAthletes().isEmpty()) {
                    return ResponseEntity.status(404).body("No athelete find at here..");
                }
                return ResponseEntity.status(200).body(response);
            } 
            // Old way
            else {
                List<AthleteRequestDTO> list = atheleteService.findByAgeLessThan(_age);
                if (list == null || list.isEmpty()) {
                    return ResponseEntity.status(404).body("No athelete find at here..");
                }
                return ResponseEntity.status(200).body(list);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Please enter a valid age...");
        }
    }
    
    // 🔥 BACKWARD COMPATIBLE: Old URL still works
    @GetMapping("/searchByHeight")
    public ResponseEntity<?> searchAtheleteByHeight(
            @RequestParam String height,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        try {
            double _height = Double.parseDouble(height);
            
            if (page != null || size != null) {
                int pageNum = page != null ? Math.max(0, page) : DEFAULT_PAGE;
                int pageSize = size != null ? Math.min(MAX_SIZE, Math.max(1, size)) : DEFAULT_SIZE;
                
                AthleteListResponseDTO response = atheleteService.findByHeightGreaterThan(_height, pageNum, pageSize);
                return ResponseEntity.status(200).body(response);
            } else {
                List<AthleteRequestDTO> list = atheleteService.findByHeightGreaterThan(_height);
                return ResponseEntity.status(200).body(list);
            }
        } catch(Exception e) {
            return ResponseEntity.status(400).body("Have to input the valid height at here..");
        }
    }
    
    // 🔥 BACKWARD COMPATIBLE: Old URL still works
    @GetMapping("/findByTeamName")
    public ResponseEntity<?> findByTeamName(
            @RequestParam String teamName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        try {
            if (page != null || size != null) {
                int pageNum = page != null ? Math.max(0, page) : DEFAULT_PAGE;
                int pageSize = size != null ? Math.min(MAX_SIZE, Math.max(1, size)) : DEFAULT_SIZE;
                
                AthleteListResponseDTO response = atheleteService.searchAtheleteByTeamName(teamName, pageNum, pageSize);
                if(response.getAthletes().isEmpty()) {
                    return ResponseEntity.status(404).body("No athelete find at here..");
                }
                return ResponseEntity.status(200).body(response);
            } else {
                List<AthleteRequestDTO> list = atheleteService.searchAtheleteByTeamName(teamName);
                if(list == null || list.isEmpty()) {
                    return ResponseEntity.status(404).body("No athelete find at here..");
                }
                return ResponseEntity.status(200).body(list);
            }
        } catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @GetMapping("/findByWeightRange")
    public ResponseEntity<?> findByWeightRange(
            @RequestParam String minHeight, 
            @RequestParam String maxHeight,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        try {
            int _minHeight = Integer.parseInt(minHeight);
            int _maxHeight = Integer.parseInt(maxHeight);
            
            List<AthleteRequestDTO> list = atheleteService.findByWeightRange(_minHeight, _maxHeight);
            
            if(list == null || list.isEmpty()) {
                return ResponseEntity.status(404).body("No athelete find at here...");
            }
            
            return ResponseEntity.status(200).body(list);
        } catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @GetMapping("/findByAgeLessThanAndHeightGreaterThan")
    public ResponseEntity<?> findByAgeLessThanAndHeightGreaterThan(
            @RequestParam String height, 
            @RequestParam String age) {
        
        try {
            double _height = Double.parseDouble(height);
            int _age = Integer.parseInt(age);
            
            List<AthleteRequestDTO> list = atheleteService.findByAgeLessThanAndHeightGreaterThan(_age, _height);
            return ResponseEntity.status(200).body(list);
        } catch(Exception e) {
            return ResponseEntity.status(400).body("Have to input the valid height at here..");
        }
    }
    
    @GetMapping("/findByPosition")
    public ResponseEntity<?> findByPosition(
            @RequestParam String position,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        try {
            List<AthleteRequestDTO> list = atheleteService.findByPosition(position);
            
            if(list == null || list.isEmpty()) {
                return ResponseEntity.status(404).body("no athelete find at here..");
            }
            
            return ResponseEntity.status(200).body(list);
        } catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @GetMapping("/searchById")
    public ResponseEntity<?> findById(@RequestParam String id) {
        try {
            return ResponseEntity.status(200).body(atheleteService.searchByAthleteId(id));
        } catch(Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    @GetMapping("/findByEventAttendence")
    public ResponseEntity<?> findByEventAttendence(
            @RequestParam String eventName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<AthleteRequestDTO> list = atheleteService.findByEventAttendenceContainingIgnoreCase(eventName);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found for this event...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/findByGameLog")
    public ResponseEntity<?> findByGameLog(
            @RequestParam String log,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<AthleteRequestDTO> list = atheleteService.findByGameLogsContainingIgnoreCase(log);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found for this game log...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/searchByPartialTeamName")
    public ResponseEntity<?> searchByPartialTeamName(
            @RequestParam String partialName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<AthleteRequestDTO> list = atheleteService.searchByTeamNamePartial(partialName);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found matching team name...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/findByMultipleEvents")
    public ResponseEntity<?> findByMultipleEvents(
            @RequestBody List<String> eventNames,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<AthleteRequestDTO> list = atheleteService.findByMultipleEvents(eventNames);
            if (list == null || list.isEmpty())
                return ResponseEntity.status(404).body("No athlete found attending all these events...");
            return ResponseEntity.status(200).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<?> findByUserId(@RequestParam String userId) {
        try {
            Optional<AthleteRequestDTO> athlete = atheleteService.findByUserId(userId);
            if (!athlete.isPresent())
                return ResponseEntity.status(404).body("No athlete found with this userId...");
            return ResponseEntity.status(200).body(athlete);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

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

    // 🔥 BACKWARD COMPATIBLE: Old URL still works
    @GetMapping("/findByWeightLessThan")
    public ResponseEntity<?> findByWeightLessThan(
            @RequestParam String weight,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            double _weight = Double.parseDouble(weight);
            
            if (page != null || size != null) {
                int pageNum = page != null ? Math.max(0, page) : DEFAULT_PAGE;
                int pageSize = size != null ? Math.min(MAX_SIZE, Math.max(1, size)) : DEFAULT_SIZE;
                
                AthleteListResponseDTO response = atheleteService.findByWeightLessThan(_weight, pageNum, pageSize);
                if (response.getAthletes().isEmpty())
                    return ResponseEntity.status(404).body("No athlete found under this weight...");
                return ResponseEntity.status(200).body(response);
            } else {
                List<AthleteRequestDTO> list = atheleteService.findByWeightLessThan(_weight);
                if (list == null || list.isEmpty())
                    return ResponseEntity.status(404).body("No athlete found under this weight...");
                return ResponseEntity.status(200).body(list);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/updateAthelete")
    public ResponseEntity<?> updateAthelete(
            @RequestParam String id, 
            @RequestBody Athelete updatedAthelete, 
            @RequestParam String userId) {
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
package com.example.demo700.Controllers.AtheleteController;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;  // ✅ Correct for Spring Boot 3.x


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Services.Athlete.AtheleteService;

@RestController
@RequestMapping("/api/athelete")
public class AtheleteController {

    @Autowired
    private AtheleteService atheleteService;
    
    // Auto-pagination settings
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 50;

    // ==================== AUTO-PAGINATED ENDPOINTS ====================
    // Users just hit these - no page numbers needed!
    
    @GetMapping("/seeAll")
    public ResponseEntity<?> seeAllAthelete(HttpServletRequest request) {
        return handleAutoPagination(request, null, null, null);
    }
    
    @GetMapping("/searchByAge")
    public ResponseEntity<?> searchAteleteByAge(
            @RequestParam String age,
            HttpServletRequest request) {
        return handleAutoPagination(request, "age", age, null);
    }
    
    @GetMapping("/searchByHeight")
    public ResponseEntity<?> searchAtheleteByHeight(
            @RequestParam String height,
            HttpServletRequest request) {
        return handleAutoPagination(request, "height", height, null);
    }
    
    @GetMapping("/findByTeamName")
    public ResponseEntity<?> findByTeamName(
            @RequestParam String teamName,
            HttpServletRequest request) {
        return handleAutoPagination(request, "team", teamName, null);
    }
    
    @GetMapping("/findByWeightLessThan")
    public ResponseEntity<?> findByWeightLessThan(
            @RequestParam String weight,
            HttpServletRequest request) {
        return handleAutoPagination(request, "weight", weight, null);
    }
    
    @GetMapping("/findByPosition")
    public ResponseEntity<?> findByPosition(
            @RequestParam String position,
            HttpServletRequest request) {
        return handleAutoPagination(request, "position", position, null);
    }
    
    @GetMapping("/findByEventAttendence")
    public ResponseEntity<?> findByEventAttendence(
            @RequestParam String eventName,
            HttpServletRequest request) {
        return handleAutoPagination(request, "event", eventName, null);
    }
    
    @GetMapping("/findByGameLog")
    public ResponseEntity<?> findByGameLog(
            @RequestParam String log,
            HttpServletRequest request) {
        return handleAutoPagination(request, "gameLog", log, null);
    }
    
    @GetMapping("/searchByPartialTeamName")
    public ResponseEntity<?> searchByPartialTeamName(
            @RequestParam String partialName,
            HttpServletRequest request) {
        return handleAutoPagination(request, "partialTeam", partialName, null);
    }
    
    @GetMapping("/findByWeightRange")
    public ResponseEntity<?> findByWeightRange(
            @RequestParam String minHeight, 
            @RequestParam String maxHeight,
            HttpServletRequest request) {
        return handleAutoPagination(request, "weightRange", minHeight + "," + maxHeight, null);
    }
    
    @GetMapping("/findByAgeLessThanAndHeightGreaterThan")
    public ResponseEntity<?> findByAgeLessThanAndHeightGreaterThan(
            @RequestParam String height, 
            @RequestParam String age,
            HttpServletRequest request) {
        return handleAutoPagination(request, "ageHeight", age + "," + height, null);
    }
    
    @PostMapping("/findByMultipleEvents")
    public ResponseEntity<?> findByMultipleEvents(
            @RequestBody List<String> eventNames,
            HttpServletRequest request) {
        return handleAutoPagination(request, "multipleEvents", 
                                   String.join(",", eventNames), null);
    }
    
    // ==================== PAGINATION NAVIGATION ENDPOINTS ====================
    
    @GetMapping("/next")
    public ResponseEntity<?> nextPage(
            @RequestParam String currentUrl,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchValue,
            HttpServletRequest request) {
        
        try {
            // Extract current page from URL
            int currentPage = extractPageFromUrl(currentUrl);
            int nextPage = currentPage + 1;
            
            return handleAutoPagination(request, searchType, searchValue, nextPage);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error navigating to next page: " + e.getMessage());
        }
    }
    
    @GetMapping("/prev")
    public ResponseEntity<?> prevPage(
            @RequestParam String currentUrl,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchValue,
            HttpServletRequest request) {
        
        try {
            int currentPage = extractPageFromUrl(currentUrl);
            int prevPage = Math.max(0, currentPage - 1);
            
            return handleAutoPagination(request, searchType, searchValue, prevPage);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error navigating to previous page: " + e.getMessage());
        }
    }
    
    // ==================== CORE PAGINATION HANDLER ====================
    
    private ResponseEntity<?> handleAutoPagination(
            HttpServletRequest request,
            String searchType,
            String searchValue,
            Integer specificPage) {
        
        try {
            // Auto-detect page from request
            int page = specificPage != null ? specificPage : detectPageFromRequest(request);
            int size = detectSizeFromRequest(request);
            
            // Validate page and size
            page = Math.max(0, page);
            size = Math.min(MAX_SIZE, Math.max(1, size));
            
            AthleteListResponseDTO response = null;
            
            // Perform search based on type
            if (searchType == null || searchValue == null) {
                response = atheleteService.seeAll(page, size);
            } else {
                switch (searchType) {
                    case "age":
                        response = atheleteService.findByAgeLessThan(Integer.parseInt(searchValue), page, size);
                        break;
                    case "height":
                        response = atheleteService.findByHeightGreaterThan(Double.parseDouble(searchValue), page, size);
                        break;
                    case "weight":
                        response = atheleteService.findByWeightLessThan(Double.parseDouble(searchValue), page, size);
                        break;
                    case "team":
                        response = atheleteService.searchAtheleteByTeamName(searchValue, page, size);
                        break;
                    case "position":
                        response = atheleteService.findByPosition(searchValue, page, size);
                        break;
                    case "event":
                        response = atheleteService.findByEventAttendenceContainingIgnoreCase(searchValue, page, size);
                        break;
                    case "gameLog":
                        response = atheleteService.findByGameLogsContainingIgnoreCase(searchValue, page, size);
                        break;
                    case "partialTeam":
                        response = atheleteService.searchByTeamNamePartial(searchValue, page, size);
                        break;
                    case "weightRange":
                        String[] weights = searchValue.split(",");
                        double min = Double.parseDouble(weights[0]);
                        double max = Double.parseDouble(weights[1]);
                        response = atheleteService.findByWeightRange(min, max, page, size);
                        break;
                    case "ageHeight":
                        String[] params = searchValue.split(",");
                        int age = Integer.parseInt(params[0]);
                        double height = Double.parseDouble(params[1]);
                        response = atheleteService.findByAgeLessThanAndHeightGreaterThan(age, height, page, size);
                        break;
                    case "multipleEvents":
                        List<String> events = List.of(searchValue.split(","));
                        response = atheleteService.findByMultipleEvents(events, page, size);
                        break;
                    default:
                        response = atheleteService.seeAll(page, size);
                }
            }
            
            if (response.getAthletes().isEmpty()) {
                return ResponseEntity.status(404).body("No athletes found");
            }
            
            // Add navigation links
            addNavigationLinks(response, request, searchType, searchValue);
            
            // Add helpful metadata
            response.setMessage("Auto-pagination applied. Use nextLink/prevLink for navigation.");
            response.setSuggestedPageSize(String.valueOf(DEFAULT_SIZE));
            response.setCurrentUrl(request.getRequestURI());
            
            return ResponseEntity.status(200).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    private int detectPageFromRequest(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                return Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                return DEFAULT_PAGE;
            }
        }
        return DEFAULT_PAGE;
    }
    
    private int detectSizeFromRequest(HttpServletRequest request) {
        String sizeParam = request.getParameter("size");
        if (sizeParam != null) {
            try {
                int size = Integer.parseInt(sizeParam);
                return Math.min(MAX_SIZE, Math.max(1, size));
            } catch (NumberFormatException e) {
                return DEFAULT_SIZE;
            }
        }
        return DEFAULT_SIZE;
    }
    
    private int extractPageFromUrl(String url) {
        try {
            if (url.contains("page=")) {
                String pagePart = url.substring(url.indexOf("page=") + 5);
                if (pagePart.contains("&")) {
                    pagePart = pagePart.substring(0, pagePart.indexOf("&"));
                }
                return Integer.parseInt(pagePart);
            }
        } catch (Exception e) {
            // Fallback to default
        }
        return DEFAULT_PAGE;
    }
    
    private void addNavigationLinks(AthleteListResponseDTO response, 
                                    HttpServletRequest request,
                                    String searchType, 
                                    String searchValue) {
        
        String baseUrl = request.getRequestURI();
        int currentPage = response.getCurrentPage();
        int pageSize = response.getPageSize();
        int totalPages = response.getTotalPages();
        
        // Build search parameters string
        String searchParams = "";
        if (searchType != null && searchValue != null) {
            switch (searchType) {
                case "age":
                    searchParams = "&age=" + searchValue;
                    break;
                case "height":
                    searchParams = "&height=" + searchValue;
                    break;
                case "weight":
                    searchParams = "&weight=" + searchValue;
                    break;
                case "team":
                    searchParams = "&teamName=" + searchValue;
                    break;
                case "position":
                    searchParams = "&position=" + searchValue;
                    break;
                case "event":
                    searchParams = "&eventName=" + searchValue;
                    break;
                case "gameLog":
                    searchParams = "&log=" + searchValue;
                    break;
                case "partialTeam":
                    searchParams = "&partialName=" + searchValue;
                    break;
            }
        }
        
        // Build navigation links
        response.setSelfLink(baseUrl + "?page=" + currentPage + "&size=" + pageSize + searchParams);
        
        if (currentPage < totalPages - 1) {
            response.setNextLink("/api/athelete/next?currentUrl=" + response.getSelfLink() + 
                                "&searchType=" + (searchType != null ? searchType : "") +
                                "&searchValue=" + (searchValue != null ? searchValue : ""));
        }
        
        if (currentPage > 0) {
            response.setPrevLink("/api/athelete/prev?currentUrl=" + response.getSelfLink() +
                                "&searchType=" + (searchType != null ? searchType : "") +
                                "&searchValue=" + (searchValue != null ? searchValue : ""));
        }
        
        response.setFirstLink(baseUrl + "?page=0&size=" + pageSize + searchParams);
        
        if (totalPages > 0) {
            response.setLastLink(baseUrl + "?page=" + (totalPages - 1) + "&size=" + pageSize + searchParams);
        }
        
        response.setSearchParams(searchParams);
    }
    
    // ==================== SINGLE RESULT ENDPOINTS (No pagination needed) ====================
    
    @GetMapping("/searchById")
    public ResponseEntity<?> findById(@RequestParam String id) {
        try {
            return ResponseEntity.status(200).body(atheleteService.searchByAthleteId(id));
        } catch(Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
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
    
    // ==================== CRUD OPERATIONS ====================
    
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
    
    @GetMapping("/count")
    public ResponseEntity<?> getTotalCount() {
        try {
            long count = atheleteService.getTotalAthleteCount();
            return ResponseEntity.status(200).body("Total athletes: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting count: " + e.getMessage());
        }
    }
}
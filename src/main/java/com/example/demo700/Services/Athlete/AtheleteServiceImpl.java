package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.AthleteClassification;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Repositories.UserGenderRepository;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.AthleteClassificationRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Validator.URLValidator;

@Service
public class AtheleteServiceImpl implements AtheleteService {

    @Autowired
    AtheleteRepository athleteRepository;

    @Autowired
    AthleteLocationRepository athleteLocationRepository;

    @Autowired
    AthleteClassificationRepository athleteClassificationRepository;

    @Autowired
    UserGenderRepository athleteGenderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MatchRepository matchRepository;

    URLValidator urlValidator = new URLValidator();

    @Autowired
    CyclicCleaner cleaner;

    @Override
    public Athelete addAthelete(Athelete athelete, String userId) {

        if (athelete == null || userId == null) {
            throw new NullPointerException("Have to take input of all data...");
        }

        User _user = userRepository.findById(userId)
            .orElseThrow(() -> new ArithmeticException("False user's request..."));

        if (!_user.getRoles().contains(Role.ROLE_ADMIN) && !_user.getRoles().contains(Role.ROLE_ATHLETE)) {
            throw new ArithmeticException("Only admin can add a player...");
        }

        System.out.println(athelete.toString());

        User user = userRepository.findById(athelete.getUserId())
            .orElseThrow(() -> new ArithmeticException("Wrong user id..."));

        if (!user.getRoles().contains(Role.ROLE_ATHLETE)) {
            throw new ArithmeticException("False user request...");
        }

        Optional<Athelete> existingAthlete = athleteRepository.findByUserId(athelete.getUserId());
        if (existingAthlete.isPresent()) {
            return null;
        }

        System.out.println("I am before the athelete saving...");

        if (athelete.getHighlightReels() != null) {
            if (!urlValidator.isValid(athelete.getHighlightReels())) {
                throw new ArithmeticException("Your match rell url's are not valid...");
            }
        }

        if (athelete.getGameLogs() != null) {
            if (!urlValidator.isValid(athelete.getGameLogs())) {
                throw new ArithmeticException("Your game logs url are not valid....");
            }
        }

        if (athelete.getPresentTeam() != null) {
            throw new ArithmeticException("No athlete can join a team in time of creation...");
        }

        if (athelete.getEventAttendence() != null && !athelete.getEventAttendence().isEmpty()) {
            throw new ArithmeticException("Match information is not valid...");
        }

        if (athelete.getAge() < 5) {
            throw new ArithmeticException("Age less than 5 years can't be an athlete...");
        }

        if (athelete.getHeight() < 3.0) {
            throw new ArithmeticException("Any atheltes height can't be less than 3");
        }

        athelete = athleteRepository.save(athelete);

        if (athelete == null) {
            return null;
        }

        return athelete;
    }

    // 🔥 FIXED: Paginated version
    @Override
    public AthleteListResponseDTO seeAll(int page, int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(1, size));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Athelete> athletePage = athleteRepository.findAll(pageable);
        
        if (!athletePage.hasContent()) {
            return new AthleteListResponseDTO(new ArrayList<>(), page, size, 
                                              athletePage.getTotalElements(), 
                                              athletePage.getTotalPages());
        }
        
        List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());
        
        return new AthleteListResponseDTO(athletes, page, size, 
                                          athletePage.getTotalElements(), 
                                          athletePage.getTotalPages());
    }
    
    // 🔥 Deprecated version
    @Override
    @Deprecated
    public List<AthleteRequestDTO> seeAll() {
        throw new UnsupportedOperationException(
            "This method is disabled for large databases. Use seeAll(page, size) instead.");
    }

    @Override
    public Athelete updateAthelete(Athelete athelete, String userId, String atheleteId) {

        if (athelete == null || userId == null || atheleteId == null) {
            throw new NullPointerException("Have to take input of all data...");
        }

        Athelete _athelete = athleteRepository.findById(atheleteId)
            .orElseThrow(() -> new ArithmeticException("No athelete present to update..."));

        User _user = userRepository.findById(userId)
            .orElseThrow(() -> new ArithmeticException("False user's request..."));

        if (!_user.getRoles().contains(Role.ROLE_ATHLETE) || !_user.getId().equals(_athelete.getUserId())) {
            throw new ArithmeticException("Only athelete can update ownself...");
        }

        System.out.println(athelete.toString());

        User user = userRepository.findById(athelete.getUserId())
            .orElseThrow(() -> new ArithmeticException("Wrong user id..."));

        Athelete __athlete = athleteRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ArithmeticException("Athlete not found"));

        if (!user.getRoles().contains(Role.ROLE_ATHLETE) || !__athlete.getId().equals(atheleteId)) {
            throw new ArithmeticException("False user request...");
        }

        try {
            if (__athlete.getPresentTeam() != null) {
                Team team1 = teamRepository.findByTeamNameIgnoreCase(__athlete.getPresentTeam());

                if (team1 == null) {
                    throw new Exception();
                }

                if (!team1.getAtheletes().contains(atheleteId)) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            throw new ArithmeticException("Team information is not valid...");
        }

        System.out.println("I am before the athelete saving...");

        if (athelete.getHighlightReels() != null) {
            if (!urlValidator.isValid(athelete.getHighlightReels())) {
                throw new ArithmeticException("Your match rell url's are not valid...");
            }
        }

        if (athelete.getGameLogs() != null) {
            if (!urlValidator.isValid(athelete.getGameLogs())) {
                throw new ArithmeticException("Your game logs url are not valid....");
            }
        }

        try {
            if (athelete.getEventAttendence() != null && !athelete.getEventAttendence().isEmpty()) {
                for (String i : athelete.getEventAttendence()) {
                    Match match = matchRepository.findById(i)
                        .orElseThrow(() -> new Exception());

                    List<Team> teams = teamRepository.findByMatchesContainingIgnoreCase(match.getId());

                    if (teams.isEmpty()) {
                        throw new Exception();
                    }

                    boolean find = false;
                    for (Team team : teams) {
                        if (team.getAtheletes().contains(_athelete.getId())) {
                            find = true;
                            break;
                        }
                    }

                    if (!find) {
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
            throw new ArithmeticException("Match information is not valid...");
        }

        if (athelete.getAge() < 5) {
            throw new ArithmeticException("Age less than 5 years can't be an athlete...");
        }

        if (athelete.getHeight() < 3.0) {
            throw new ArithmeticException("Any atheltes height can't be less than 3");
        }

        athelete.setId(_athelete.getId());
        athelete = athleteRepository.save(athelete);

        if (athelete == null) {
            return null;
        }

        return athelete;
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean deleteAthelete(String atheleteId, String userId) {

        if (atheleteId == null || userId == null) {
            throw new NullPointerException("false request...");
        }

        Athelete athelete = athleteRepository.findById(atheleteId)
            .orElseThrow(() -> new ArithmeticException("No such athelete present at here..."));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ArithmeticException("No such user present at here..."));

        if (user.getRoles().contains(Role.ROLE_ADMIN) || 
            athleteRepository.findByUserId(userId).map(Athelete::getId).orElse("").equals(atheleteId)) {

            long count = athleteRepository.count();
            cleaner.removeAthelete(atheleteId);
            boolean yes = count != athleteRepository.count();

            if (yes) {
                try {
                    Team team = teamRepository.findByAtheletesContainingIgnoreCase(atheleteId);
                    if (team != null) {
                        team.getAtheletes().remove(atheleteId);
                    }
                } catch (Exception e) {
                    // Log error but continue
                }
            }
            return yes;
        } else {
            return false;
        }
    }

    // 🔥 FIXED: Search with pagination
    @Override
    public AthleteListResponseDTO searchAtheleteByTeamName(String teamName, int page, int size) {
        if (teamName == null) {
            throw new NullPointerException("No athelete find at here...");
        }

        page = Math.max(0, page);
        size = Math.min(100, Math.max(1, size));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Athelete> athletePage = athleteRepository.findByPresentTeamIgnoreCase(teamName, pageable);
        
        if (!athletePage.hasContent()) {
            return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
        }
        
        List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());
        
        return new AthleteListResponseDTO(athletes, page, size, 
                                          athletePage.getTotalElements(), 
                                          athletePage.getTotalPages());
    }

    // 🔥 FIXED: Search with pagination
    @Override
    public AthleteListResponseDTO findByAgeLessThan(int age, int page, int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(1, size));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Athelete> athletePage = athleteRepository.findByAgeLessThan(age, pageable);
        
        if (!athletePage.hasContent()) {
            return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
        }
        
        List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());
        
        return new AthleteListResponseDTO(athletes, page, size, 
                                          athletePage.getTotalElements(), 
                                          athletePage.getTotalPages());
    }

    // 🔥 FIXED: Search with pagination
    @Override
    public AthleteListResponseDTO findByHeightGreaterThan(double height, int page, int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(1, size));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Athelete> athletePage = athleteRepository.findByHeightGreaterThan(height, pageable);
        
        if (!athletePage.hasContent()) {
            return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
        }
        
        List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());
        
        return new AthleteListResponseDTO(athletes, page, size, 
                                          athletePage.getTotalElements(), 
                                          athletePage.getTotalPages());
    }

    // 🔥 FIXED: Search with pagination
    @Override
    public AthleteListResponseDTO findByWeightLessThan(double weight, int page, int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(1, size));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Athelete> athletePage = athleteRepository.findByWeightLessThan(weight, pageable);
        
        if (!athletePage.hasContent()) {
            return new AthleteListResponseDTO(new ArrayList<>(), page, size, 0, 0);
        }
        
        List<AthleteRequestDTO> athletes = getListDetailsFromAthleteList(athletePage.getContent());
        
        return new AthleteListResponseDTO(athletes, page, size, 
                                          athletePage.getTotalElements(), 
                                          athletePage.getTotalPages());
    }

    // 🔥 COMPLETED: Non-paginated version for backward compatibility
    @Override
    public List<AthleteRequestDTO> searchAtheleteByTeamName(String teamName) {
        if (teamName == null) {
            throw new NullPointerException("False request....");
        }
        
        List<Athelete> athletes = athleteRepository.findByPresentTeamIgnoreCase(teamName);
        
        if (athletes.isEmpty()) {
            throw new NoSuchElementException("No athletes found for team: " + teamName);
        }
        
        return getListDetailsFromAthleteList(athletes);
    }

    // 🔥 COMPLETED: Non-paginated version for backward compatibility
    @Override
    public List<AthleteRequestDTO> findByAgeLessThan(int age) {
        List<Athelete> athletes = athleteRepository.findByAgeLessThan(age);
        
        if (athletes.isEmpty()) {
            throw new NoSuchElementException("No athletes found with age less than: " + age);
        }
        
        return getListDetailsFromAthleteList(athletes);
    }

    // 🔥 COMPLETED: Non-paginated version for backward compatibility
    @Override
    public List<AthleteRequestDTO> findByHeightGreaterThan(double height) {
        List<Athelete> athletes = athleteRepository.findByHeightGreaterThan(height);
        
        if (athletes.isEmpty()) {
            throw new NoSuchElementException("No athletes found with height greater than: " + height);
        }
        
        return getListDetailsFromAthleteList(athletes);
    }

    // 🔥 COMPLETED: Non-paginated version for backward compatibility
    @Override
    public List<AthleteRequestDTO> findByWeightLessThan(double weight) {
        List<Athelete> athletes = athleteRepository.findByWeightLessThan(weight);
        
        if (athletes.isEmpty()) {
            throw new NoSuchElementException("No athletes found with weight less than: " + weight);
        }
        
        return getListDetailsFromAthleteList(athletes);
    }

    // Keep original methods for backward compatibility
    @Override
    public List<AthleteRequestDTO> findByPresentTeamIgnoreCase(String teamName) {
        if (teamName == null) {
            throw new NullPointerException("Invalid request...");
        }
        List<Athelete> list = athleteRepository.findByPresentTeamIgnoreCase(teamName);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public List<AthleteRequestDTO> findByPosition(String position) {
        List<Athelete> list = athleteRepository.findByPositionContainingIgnoreCase(position);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public List<AthleteRequestDTO> findByEventAttendenceContainingIgnoreCase(String eventName) {
        if (eventName == null) {
            throw new NullPointerException("Invalid request...");
        }
        List<Athelete> list = athleteRepository.findByEventAttendenceContainingIgnoreCase(eventName);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public List<AthleteRequestDTO> findByGameLogsContainingIgnoreCase(String gameLog) {
        if (gameLog == null) {
            throw new NullPointerException("Invalid request...");
        }
        List<Athelete> list = athleteRepository.findByGameLogsContainingIgnoreCase(gameLog);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public List<AthleteRequestDTO> findByAgeLessThanAndHeightGreaterThan(int age, double height) {
        List<Athelete> list = athleteRepository.findByAgeLessThanAndHeightGreaterThan(age, height);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public List<AthleteRequestDTO> searchByTeamNamePartial(String partialName) {
        if (partialName == null) {
            throw new NullPointerException("Invalid request...");
        }
        List<Athelete> list = athleteRepository.searchByTeamNamePartial(partialName);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public List<AthleteRequestDTO> findByMultipleEvents(List<String> eventNames) {
        if (eventNames == null) {
            throw new NullPointerException("Invalid request...");
        }
        List<Athelete> list = athleteRepository.findByMultipleEvents(eventNames);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public List<AthleteRequestDTO> findByWeightRange(double min, double max) {
        List<Athelete> list = athleteRepository.findByWeightRange(min, max);
        if (list.isEmpty()) {
            throw new NoSuchElementException("No athletes found...");
        }
        return getListDetailsFromAthleteList(list);
    }

    @Override
    public Optional<AthleteRequestDTO> findByUserId(String userId) {
        if (userId == null) {
            throw new NullPointerException("Invalid request...");
        }

        Optional<Athelete> athleteOpt = athleteRepository.findByUserId(userId);
        if (athleteOpt.isEmpty()) {
            throw new NoSuchElementException("No athlete found...");
        }

        AthleteRequestDTO dto = getDetailsFromAthleteId(athleteOpt.get().getId());
        return Optional.of(dto);
    }

    public AthleteRequestDTO searchByAthleteId(String athleteId) {
        if (athleteId == null) {
            throw new NullPointerException("False request...");
        }
        try {
            return getDetailsFromAthleteId(athleteId);
        } catch (Exception e) {
            throw new NoSuchElementException("No such athlete fin at here...");
        }
    }

    @Override
    public boolean deleteByUserId(String userId, String actionUserId) {
        if (userId == null || actionUserId == null) {
            throw new NullPointerException("false request...");
        }

        User user = userRepository.findById(actionUserId)
            .orElseThrow(() -> new ArithmeticException("unsafe operation..."));

        Athelete athelete = athleteRepository.findById(userId)
            .orElse(null);

        if (athelete == null) {
            return false;
        }

        if (!user.getRoles().contains(Role.ROLE_ADMIN) && !athelete.getUserId().equals(actionUserId)) {
            throw new ArithmeticException("Only center admin can remove atheletes...");
        }

        long count = athleteRepository.count();
        cleaner.removeAthelete(athelete.getId());
        boolean yes = count != athleteRepository.count();
        return yes;
    }

    // 🔥 Optimized batch fetching with error handling
    public List<AthleteRequestDTO> getListDetailsFromAthleteList(List<Athelete> athletes) {
        if (athletes == null || athletes.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Limit batch size to prevent memory overflow
        if (athletes.size() > 500) {
            throw new IllegalArgumentException("Batch size too large. Max 500 athletes per batch.");
        }

        // Extract IDs
        List<String> athleteIds = athletes.stream()
            .map(Athelete::getId)
            .collect(Collectors.toList());
        
        List<String> userIds = athletes.stream()
            .map(Athelete::getUserId)
            .collect(Collectors.toList());

        // Batch fetch with error handling
        Map<String, User> userMap = new HashMap<>();
        Map<String, AthleteLocation> locationMap = new HashMap<>();
        Map<String, UserGender> genderMap = new HashMap<>();
        Map<String, AthleteClassification> classificationMap = new HashMap<>();

        try {
            List<User> users = userRepository.findAllById(userIds);
            for (User u : users) {
                userMap.put(u.getId(), u);
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }

        try {
            List<AthleteLocation> locations = athleteLocationRepository.findByAthleteIdIn(athleteIds);
            for (AthleteLocation l : locations) {
                locationMap.put(l.getAthleteId(), l);
            }
        } catch (Exception e) {
            System.err.println("Error fetching locations: " + e.getMessage());
        }

        try {
            List<UserGender> genders = athleteGenderRepository.findByUserIdIn(userIds);
            for (UserGender g : genders) {
                genderMap.put(g.getUserId(), g);
            }
        } catch (Exception e) {
            System.err.println("Error fetching genders: " + e.getMessage());
        }

        try {
            List<AthleteClassification> classifications = athleteClassificationRepository.findByAthleteIdIn(athleteIds);
            for (AthleteClassification c : classifications) {
                classificationMap.put(c.getAthleteId(), c);
            }
        } catch (Exception e) {
            System.err.println("Error fetching classifications: " + e.getMessage());
        }

        // Build DTOs
        List<AthleteRequestDTO> dtoList = new ArrayList<>(athletes.size());
        
        for (Athelete a : athletes) {
            try {
                AthleteRequestDTO dto = new AthleteRequestDTO();
                
                dto.setId(a.getId());
                dto.setAge(a.getAge());
                dto.setHeight(a.getHeight());
                dto.setWeight(a.getWeight());
                dto.setUserId(a.getUserId());
                dto.setPosition(a.getPosition());
                dto.setPresentTeam(a.getPresentTeam());
                dto.setGameLogs(a.getGameLogs());
                dto.setEventAttendence(a.getEventAttendence());
                dto.setHighlightReels(a.getHighlightReels());
                
                User user = userMap.get(a.getUserId());
                if (user != null) {
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setRoles(user.getRoles());
                }
                
                AthleteLocation loc = locationMap.get(a.getId());
                if (loc != null) {
                    dto.setLattitude(loc.getLattitude());
                    dto.setLongitude(loc.getLongitude());
                    dto.setLocationName(loc.getLocationName());
                }
                
                UserGender gender = genderMap.get(a.getUserId());
                if (gender != null) {
                    dto.setGender(gender.getGender());
                }
                
                AthleteClassification cls = classificationMap.get(a.getId());
                if (cls != null) {
                    dto.setAthleteClassificationTypes(cls.getAthleteClassificationTypes());
                }
                
                dtoList.add(dto);
            } catch (Exception e) {
                System.err.println("Error building DTO for athlete: " + a.getId());
                // Skip problematic athlete but continue processing others
            }
        }
        
        return dtoList;
    }

    private AthleteRequestDTO getDetailsFromAthleteId(String athleteId) {
        Athelete athleteDetails = athleteRepository.findById(athleteId)
            .orElseThrow(() -> new NoSuchElementException("Athlete not found"));
        
        AthleteRequestDTO athlete = new AthleteRequestDTO();
        
        athlete.setId(athleteDetails.getId());
        athlete.setAge(athleteDetails.getAge());
        athlete.setGameLogs(athleteDetails.getGameLogs());
        athlete.setEventAttendence(athleteDetails.getEventAttendence());
        athlete.setHeight(athleteDetails.getHeight());
        athlete.setWeight(athleteDetails.getWeight());
        athlete.setUserId(athleteDetails.getUserId());
        athlete.setPosition(athleteDetails.getPosition());
        athlete.setPresentTeam(athleteDetails.getPresentTeam());
        athlete.setHighlightReels(athleteDetails.getHighlightReels());

        User user = userRepository.findById(athleteDetails.getUserId())
            .orElseThrow(() -> new NoSuchElementException("User not found"));
        
        athlete.setName(user.getName());
        athlete.setEmail(user.getEmail());
        athlete.setRoles(user.getRoles());

        try {
            AthleteLocation location = athleteLocationRepository.findByAthleteId(athleteId);
            if (location != null) {
                //AthleteLocation loc = location.get();
                athlete.setLattitude(location.getLattitude());
                athlete.setLongitude(location.getLongitude());
                athlete.setLocationName(location.getLocationName());
            }
        } catch (Exception e) {
            // Location not found, continue
        }

        try {
            UserGender userGender = athleteGenderRepository.findByUserId(athleteDetails.getUserId());
            if (userGender != null) {
                athlete.setGender(userGender.getGender());
            }
        } catch (Exception e) {
            // Gender not found, continue
        }

        try {
            AthleteClassification classification = athleteClassificationRepository.findByAthleteId(athleteId);
            if (classification != null) {
                athlete.setAthleteClassificationTypes(classification.getAthleteClassificationTypes());
            }
        } catch (Exception e) {
            // Classification not found, continue
        }

        return athlete;
    }
    
    // 🔥 Helper method to get total count
    public long getTotalAthleteCount() {
        return athleteRepository.count();
    }
}
package com.example.demo700.Services.Athlete;

import java.util.List;
import java.util.Optional;

import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.Models.Athlete.Athelete;

public interface AtheleteService {
    
    Athelete addAthelete(Athelete athelete, String userId);
    
    // 🔥 New paginated methods
    AthleteListResponseDTO seeAll(int page, int size);
    AthleteListResponseDTO searchAtheleteByTeamName(String teamName, int page, int size);
    AthleteListResponseDTO findByAgeLessThan(int age, int page, int size);
    AthleteListResponseDTO findByHeightGreaterThan(double height, int page, int size);
    AthleteListResponseDTO findByWeightLessThan(double weight, int page, int size);
    
    // Old methods (deprecated)
    @Deprecated
    List<AthleteRequestDTO> seeAll();
    @Deprecated
    List<AthleteRequestDTO> searchAtheleteByTeamName(String teamName);
    @Deprecated
    List<AthleteRequestDTO> findByAgeLessThan(int age);
    @Deprecated
    List<AthleteRequestDTO> findByHeightGreaterThan(double height);
    @Deprecated
    List<AthleteRequestDTO> findByWeightLessThan(double weight);
    
    // Keep other existing methods
    Athelete updateAthelete(Athelete athelete, String userId, String atheleteId);
    boolean deleteAthelete(String atheleteId, String userId);
    List<AthleteRequestDTO> findByPresentTeamIgnoreCase(String teamName);
    List<AthleteRequestDTO> findByPosition(String position);
    List<AthleteRequestDTO> findByEventAttendenceContainingIgnoreCase(String eventName);
    List<AthleteRequestDTO> findByGameLogsContainingIgnoreCase(String gameLog);
    List<AthleteRequestDTO> findByAgeLessThanAndHeightGreaterThan(int age, double height);
    List<AthleteRequestDTO> searchByTeamNamePartial(String partialName);
    List<AthleteRequestDTO> findByMultipleEvents(List<String> eventNames);
    List<AthleteRequestDTO> findByWeightRange(double min, double max);
    Optional<AthleteRequestDTO> findByUserId(String userId);
    AthleteRequestDTO searchByAthleteId(String athleteId);
    boolean deleteByUserId(String userId, String actionUserId);
}
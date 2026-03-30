package com.example.demo700.Services.Athlete;

import java.util.List;
import java.util.Optional;

import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.DTOFiles.AthleteListResponseDTO;
import com.example.demo700.Models.Athlete.Athelete;

public interface AtheleteService {
    
    // CRUD Operations
    Athelete addAthelete(Athelete athelete, String userId);
    Athelete updateAthelete(Athelete athelete, String userId, String atheleteId);
    boolean deleteAthelete(String atheleteId, String userId);
    boolean deleteByUserId(String userId, String actionUserId);
    
    // 🔥 PAGINATED SEARCH METHODS
    AthleteListResponseDTO seeAll(int page, int size);
    AthleteListResponseDTO searchAtheleteByTeamName(String teamName, int page, int size);
    AthleteListResponseDTO findByAgeLessThan(int age, int page, int size);
    AthleteListResponseDTO findByHeightGreaterThan(double height, int page, int size);
    AthleteListResponseDTO findByWeightLessThan(double weight, int page, int size);
    AthleteListResponseDTO findByPresentTeamIgnoreCase(String teamName, int page, int size);
    AthleteListResponseDTO findByPosition(String position, int page, int size);
    AthleteListResponseDTO findByEventAttendenceContainingIgnoreCase(String eventName, int page, int size);
    AthleteListResponseDTO findByGameLogsContainingIgnoreCase(String gameLog, int page, int size);
    AthleteListResponseDTO findByAgeLessThanAndHeightGreaterThan(int age, double height, int page, int size);
    AthleteListResponseDTO searchByTeamNamePartial(String partialName, int page, int size);
    AthleteListResponseDTO findByMultipleEvents(List<String> eventNames, int page, int size);
    AthleteListResponseDTO findByWeightRange(double min, double max, int page, int size);
    AthleteListResponseDTO findByGender(String gender, int page, int size);
    
    List<AthleteRequestDTO> findByNamePartial(String partialName);
    
    // 🔥 SINGLE RESULT METHODS
    AthleteRequestDTO searchByAthleteId(String athleteId);
    Optional<AthleteRequestDTO> findByUserId(String userId);
    long getTotalAthleteCount();
    
    // 🔥 DEPRECATED NON-PAGINATED METHODS (Keep for backward compatibility)
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
    @Deprecated
    List<AthleteRequestDTO> findByPresentTeamIgnoreCase(String teamName);
    @Deprecated
    List<AthleteRequestDTO> findByPosition(String position);
    @Deprecated
    List<AthleteRequestDTO> findByEventAttendenceContainingIgnoreCase(String eventName);
    @Deprecated
    List<AthleteRequestDTO> findByGameLogsContainingIgnoreCase(String gameLog);
    @Deprecated
    List<AthleteRequestDTO> findByAgeLessThanAndHeightGreaterThan(int age, double height);
    @Deprecated
    List<AthleteRequestDTO> searchByTeamNamePartial(String partialName);
    @Deprecated
    List<AthleteRequestDTO> findByMultipleEvents(List<String> eventNames);
    @Deprecated
    List<AthleteRequestDTO> findByWeightRange(double min, double max);
}
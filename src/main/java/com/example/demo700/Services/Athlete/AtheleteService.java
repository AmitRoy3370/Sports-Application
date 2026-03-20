package com.example.demo700.Services.Athlete;

import java.util.List;
import java.util.Optional;

import com.example.demo700.DTOFiles.AthleteRequestDTO;
import com.example.demo700.Models.Athlete.Athelete;

public interface AtheleteService {

	Athelete addAthelete(Athelete athelete, String userId);

	List<AthleteRequestDTO> seeAll();

	Athelete updateAthelete(Athelete athelete, String userId, String atheleteId);

	boolean deleteAthelete(String atheleteId, String userId);

	AthleteRequestDTO searchByAthleteId(String athleteId);
	
	List<AthleteRequestDTO> searchAtheleteByTeamName(String teamName);

	List<AthleteRequestDTO> findByAgeLessThan(int age);

	List<AthleteRequestDTO> findByHeightGreaterThan(double height);

	List<AthleteRequestDTO> findByWeightLessThan(double weight);

	List<AthleteRequestDTO> findByPresentTeamIgnoreCase(String presentTeam);

	List<AthleteRequestDTO> findByPosition(String position);

	List<AthleteRequestDTO> findByEventAttendenceContainingIgnoreCase(String eventName);

	List<AthleteRequestDTO> findByGameLogsContainingIgnoreCase(String gameLog);

	List<AthleteRequestDTO> findByAgeLessThanAndHeightGreaterThan(int age, double height);

	List<AthleteRequestDTO> searchByTeamNamePartial(String partialName);

	List<AthleteRequestDTO> findByMultipleEvents(List<String> eventNames);

	List<AthleteRequestDTO> findByWeightRange(double min, double max);

	Optional<AthleteRequestDTO> findByUserId(String userId);

	boolean deleteByUserId(String userId, String actionUser);

}

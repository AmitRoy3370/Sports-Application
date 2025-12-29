package com.example.demo700.Services.Athlete;

import java.util.List;
import java.util.Optional;

import com.example.demo700.Models.Athlete.Athelete;

public interface AtheleteService {

	Athelete addAthelete(Athelete athelete, String userId);

	List<Athelete> seeAll();

	Athelete updateAthelete(Athelete athelete, String userId, String atheleteId);

	boolean deleteAthelete(String atheleteId, String userId);

	List<Athelete> searchAtheleteByTeamName(String teamName);

	List<Athelete> findByAgeLessThan(int age);

	List<Athelete> findByHeightGreaterThan(double height);

	List<Athelete> findByWeightLessThan(double weight);

	List<Athelete> findByPresentTeamIgnoreCase(String presentTeam);

	List<Athelete> findByPosition(String position);

	List<Athelete> findByEventAttendenceContainingIgnoreCase(String eventName);

	List<Athelete> findByGameLogsContainingIgnoreCase(String gameLog);

	List<Athelete> findByAgeLessThanAndHeightGreaterThan(int age, double height);

	List<Athelete> searchByTeamNamePartial(String partialName);

	List<Athelete> findByMultipleEvents(List<String> eventNames);

	List<Athelete> findByWeightRange(double min, double max);

	Optional<Athelete> findByUserId(String userId);

	boolean deleteByUserId(String userId, String actionUser);

}

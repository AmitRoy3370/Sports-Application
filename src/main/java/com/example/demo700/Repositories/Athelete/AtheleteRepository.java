package com.example.demo700.Repositories.Athelete;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Athlete.Athelete;

@Repository
public interface AtheleteRepository extends MongoRepository<Athelete, String> {

	List<Athelete> findByAgeLessThan(int age);

	List<Athelete> findByHeightGreaterThan(double height);

	List<Athelete> findByWeightLessThan(double weight);

	List<Athelete> findByPresentTeamIgnoreCase(String presentTeam);

	List<Athelete> findByPosition(int position);

	List<Athelete> findByEventAttendenceContainingIgnoreCase(String eventName);

	List<Athelete> findByGameLogsContainingIgnoreCase(String gameLog);

	List<Athelete> findByAgeLessThanAndHeightGreaterThan(int age, double height);
	
	List<Athelete> findBypresentTeam(String teamName);

	@Query("{ 'presentTeam': { $regex: ?0, $options: 'i' } }")
	List<Athelete> searchByTeamNamePartial(String partialName);

	@Query("{ 'eventAttendence': { $all: ?0 } }")
	List<Athelete> findByMultipleEvents(List<String> eventNames);

	@Query("{ 'weight': { $gte: ?0, $lte: ?1 } }")
	List<Athelete> findByWeightRange(double min, double max);

	Optional<Athelete> findByUserId(String userId);

	long deleteByUserId(String userId);

}

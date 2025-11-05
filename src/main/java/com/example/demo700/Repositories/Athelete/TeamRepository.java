package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Athlete.Team;

@Repository
public interface TeamRepository extends MongoRepository<Team, String>{
	
	Team findByTeamName(String teamName);
	List<Team> findByTeamOwnerId(String teamOwnerId);
	Team findByAtheletesContainingIgnoreCase(String atheleteId);
	Team findByCoachesContainingIgnoreCase(String coachId);
	Team findByScoutsContainingIgnoreCase(String scoutId);
	List<Team> findByMatchesContainingIgnoreCase(String matchId);

}

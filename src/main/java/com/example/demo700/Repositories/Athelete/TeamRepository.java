package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Athlete.Team;

@Repository
public interface TeamRepository extends MongoRepository<Team, String>{
	
	Team findByTeamNameIgnoreCase(String teamName);
	List<Team> findByTeamNameContainingIgnoreCase(String teamName);
	List<Team> findByTeamOwnerId(String teamOwnerId);
	Team findByAtheletesContainingIgnoreCase(String atheleteId);
	List<Team> findByAtheletesIn(List<String> athletesId);
	Team findByCoachesContainingIgnoreCase(String coachId);
	List<Team> findByCoachesIn(List<String> coachesId);
	Team findByScoutsContainingIgnoreCase(String scoutId);
	List<Team> findByScoutsIn(List<String> scoutsId);
	List<Team> findByMatchesContainingIgnoreCase(String matchId);
	Team findByDoctorsContainingIgnoreCase(String doctorId);
	List<Team> findByDoctorsIn(List<String> doctorsId);

}

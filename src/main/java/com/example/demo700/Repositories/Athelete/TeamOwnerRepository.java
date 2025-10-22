package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Athlete.TeamOwner;

@Repository
public interface TeamOwnerRepository extends MongoRepository<TeamOwner, String> {

	TeamOwner findByAtheleteId(String atheleteId);
	TeamOwner findByTeamsContainingIgnoreCase(String teamName);
	List<TeamOwner> findByAchivementsContainingIgnoreCase(String achivement);
	List<TeamOwner> findByMatchesContainingIgnoreCase(String matchId);
	
}

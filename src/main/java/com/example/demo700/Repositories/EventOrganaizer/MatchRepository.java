package com.example.demo700.Repositories.EventOrganaizer;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.EventOrganaizer.Match;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {

	public List<Match> findByOrganaizerId(String organaizerId);
	public List<Match> findByTeamsContainingIgnoreCase(String teamId);
	public List<Match> findByGameLogsContainingIgnoreCase(String gameLogs);
	public List<Match> findByVideosContainingIgnoreCase(String video);
	public Match findByMatchStartTimeAndMatchEndTime(Instant matchStartTime, Instant matchEndTime);
	public List<Match> findByPriceGreaterThan(double price);
	@Query("{ 'matchStartTime': { $lt: ?1 }, 'matchEndTime': { $gt: ?0 } }")
	List<Match> findConflicts(Instant start, Instant end);
	
}

package com.example.demo700.Services.EventOrganaizer;

import java.time.Instant;
import java.util.List;

import com.example.demo700.Models.EventOrganaizer.Match;

public interface MatchService {
	
	public Match createMatch(Match match, String userId);
	public List<Match> seeAllMatch();
	public List<Match> findByOrganaizerId(String organaizerId);
	public List<Match> findByTeamsContainingIgnoreCase(String teamId);
	public List<Match> findByGameLogsContainingIgnoreCase(String gameLogs);
	public List<Match> findByVideosContainingIgnoreCase(String video);
	public Match findByMatchStartTimeAndMatchEndTime(Instant matchStartTime, Instant matchEndTime);
	public Match updateMatch(Match match, String userId, String matchId);
	public boolean deleteMatch(String matchId, String userId);
	public List<Match> findByPriceGreaterThan(double price);

}

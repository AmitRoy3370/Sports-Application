package com.example.demo700.Services.EventOrganaizer;

import java.time.Instant;
import java.util.List;

import com.example.demo700.DTOFiles.MatchResponse;
import com.example.demo700.Models.EventOrganaizer.Match;

public interface MatchService {
	
	public Match createMatch(Match match, String userId);
	public List<MatchResponse> seeAllMatch();
	public List<MatchResponse> findByOrganaizerId(String organaizerId);
	public List<MatchResponse> findByTeamsContainingIgnoreCase(String teamId);
	public List<MatchResponse> findByGameLogsContainingIgnoreCase(String gameLogs);
	public List<MatchResponse> findByVideosContainingIgnoreCase(String video);
	public MatchResponse findByMatchStartTimeAndMatchEndTime(Instant matchStartTime, Instant matchEndTime);
	public Match updateMatch(Match match, String userId, String matchId);
	public boolean deleteMatch(String matchId, String userId);
	public List<MatchResponse> findByPriceGreaterThan(double price);
	public MatchResponse findByMatchId(String id);

}

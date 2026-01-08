package com.example.demo700.Services.EventOrganaizer;

import java.util.List;

import com.example.demo700.Models.EventOrganaizer.MatchName;

public interface MatchNameService {
	
	public MatchName addMatchName(MatchName matchName, String userId);
	public MatchName updateMatchName(MatchName matchName, String userId, String matchNameId);
	public MatchName findByNameIgnoreCase(String name);
	public MatchName findByMatchId(String matchId);
	public List<MatchName> findByNameContainingIgnoreCase(String name);
	public MatchName findById(String id);
	public List<MatchName> seeAll();
	public boolean removeMatchName(String matchNameId, String userId);

}

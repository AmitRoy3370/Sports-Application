package com.example.demo700.Repositories.EventOrganaizer;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.EventOrganaizer.MatchName;

@Repository
public interface MatchNameRepository extends MongoRepository<MatchName, String> {

	public MatchName findByNameIgnoreCase(String name);
	public MatchName findByMatchId(String matchId);
	public List<MatchName> findByNameContainingIgnoreCase(String name);
	
}

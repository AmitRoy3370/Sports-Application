package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Athlete.TeamFiles;

@Repository
public interface TeamFilesRepository extends MongoRepository<TeamFiles, String> {

	public TeamFiles findByTeamId(String teamId);
	public List<TeamFiles> findByFilesContainingIgnoreCase(String fileId);
	
}

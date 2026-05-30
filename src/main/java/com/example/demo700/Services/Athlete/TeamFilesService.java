package com.example.demo700.Services.Athlete;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.Models.Athlete.TeamFiles;

public interface TeamFilesService {

	public TeamFiles addTeamFiles(MultipartFile files[], String userId, String teamId);
	public TeamFiles updateTeamFiles(MultipartFile files[], String userId, String teamFilesId, List<String> existingFiles, String teamId);
	
	public List<TeamFiles> seeAll();
	public TeamFiles findByTeamId(String teamId);
	public List<TeamFiles> findByFilesContainingIgnoreCase(String fileId);
	
	public boolean removeTeamFiles(String id, String userId);
	
}

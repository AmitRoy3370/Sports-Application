package com.example.demo700.Repositories.TeamLocationRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.demo700.Models.TeamLocationModels.TeamLocationModel;

@Repository
public interface TeamLocationRepository extends MongoRepository<TeamLocationModel, String> {

	public TeamLocationModel findByTeamId(String teamId);
	public List<TeamLocationModel> findByLocationNameContainingIgnoreCase(String locationName);
	public List<TeamLocationModel> findByLatitudeAndLongitude(double latitude, double longitude);
	
}

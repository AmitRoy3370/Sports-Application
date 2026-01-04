package com.example.demo700.Services.TeamLocationServices;

import com.example.demo700.Models.TeamLocationModels.TeamLocationModel;
import java.util.List;

public interface TeamLocationService {
	
	public TeamLocationModel addTeamLocation(TeamLocationModel teamLocation, String userId);
	public TeamLocationModel upadteTeamLocation(TeamLocationModel teamLocationModel, String userId, String teamLocationId);
	public List<TeamLocationModel> seeAllTeamLocation();
	public TeamLocationModel getByTeamLocationId(String id);
	public TeamLocationModel findByTeamId(String teamId);
	public List<TeamLocationModel> findByLocationNameContainingIgnoreCase(String locationName);
	public List<TeamLocationModel> findByLatitudeAndLongitude(double latitude, double longitude);
	public boolean removeTeamLocation(String userId, String teamLocationId);

}

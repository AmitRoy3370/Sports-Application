package com.example.demo700.Services.AthleteLocationService;

import java.util.List;

import com.example.demo700.Models.AthleteLocation.AthleteLocation;

public interface AthleteLocationService {
	
	public AthleteLocation addAthleteLocation(AthleteLocation athleteLocation, String userId);
	public List<AthleteLocation> seeAllAthleteLocation();
	public AthleteLocation findByAthleteId(String athleteId);
	public List<AthleteLocation> findByLocationName(String locationName);
	public List<AthleteLocation> findByLattitudeAndLongitude(double lattitude, double longitude);
	public AthleteLocation updateAthleteLocation(AthleteLocation athleteLocation, String userId, String athleteId);
	public boolean deleteAthleteLocation(String userId, String athleteId);
	
}

package com.example.demo700.Repositories.AthleteRepository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.AthleteLocation.AthleteLocation;

@Repository
public interface AthleteLocationRepository extends MongoRepository<AthleteLocation, String> {

	public AthleteLocation findByAthleteId(String athleteId);
	public List<AthleteLocation> findByLocationName(String locationName);
	public List<AthleteLocation> findByLattitudeAndLongitude(double lattitude, double longitude);
	
}

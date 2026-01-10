package com.example.demo700.Repositories.GymRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.demo700.Models.GymModels.Gyms;

@Repository
public interface GymsRepository extends MongoRepository<Gyms, String> {
	
	public List<Gyms> findByGymTrainer(String gymTrainer);
	public Gyms findByGymNameIgnoreCase(String gymName);
	public List<Gyms> findByLocationNameContainingIgnoreCase(String locationName);
	public List<Gyms> findByLatitudeAndLongtitude(double latitude, double longtitude);
	public List<Gyms> findByEntryFeeLessThanEqual(double entryFee);
	public List<Gyms> findByMonthlyFeeLessThanEqual(double monthlyFee);
	
}


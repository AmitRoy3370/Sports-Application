package com.example.demo700.Services.GymServices;

import java.util.List;

import com.example.demo700.Models.GymModels.Gyms;

public interface GymService {
	
	public Gyms addGyms(Gyms gyms);
	public Gyms updateGyms(Gyms gyms, String userId, String gymId);
	public List<Gyms> findByGymTrainer(String gymTrainer);
	public Gyms findByGymName(String gymName);
	public List<Gyms> findByLocationName(String locationName);
	public List<Gyms> findByLatitudeAndLongtitude(double latitude, double longtitude);
	public List<Gyms> findByEntryFeeLessThanOrEqual(double entryFee);
	public List<Gyms> findByMonthlyFeeLessThanOrEqual(double monthlyFee);
	public List<Gyms> seeAllGyms();
	public Gyms findById(String gymId);
	public boolean deleteGyms(String gymId, String userId);
	
}

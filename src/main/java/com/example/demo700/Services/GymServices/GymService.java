package com.example.demo700.Services.GymServices;

import java.time.Instant;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.Models.GymModels.Gyms;

public interface GymService {
	
	public Gyms addGyms(Gyms gyms, String userId, MultipartFile files[], MultipartFile coverImage);
	public Gyms updateGyms(Gyms gyms, String userId, String gymId, MultipartFile files[], MultipartFile coverImage);
	public List<Gyms> findByGymTrainer(String gymTrainer);
	public Gyms findByGymName(String gymName);
	public List<Gyms> findByLocationName(String locationName);
	public List<Gyms> findByLatitudeAndLongtitude(double latitude, double longtitude);
	public List<Gyms> findByEntryFeeLessThanOrEqual(double entryFee);
	public List<Gyms> findByMonthlyFeeLessThanOrEqual(double monthlyFee);
	public List<Gyms> seeAllGyms();
	public Gyms findById(String gymId);
	public boolean deleteGyms(String gymId, String userId);
	public Gyms findByTradeLicenseId(String tradeLicenseId);
	public Gyms findByTinNumber(String tinNumber);
	public List<Gyms> findByOpeningTimeBefore(Instant openingTime);
	public List<Gyms> findByOpeningTimeAfter(Instant openingTime);
	public List<Gyms> findByClosingTimeBefore(Instant closingTime);
	public List<Gyms> findByClosingTimeAfter(Instant closingTime);
	public List<Gyms> findByGymOwner(String gymOwner);
	public List<Gyms> findByGymNameContainingIgnoreCase(String gymName);
	
}

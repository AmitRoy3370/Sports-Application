package com.example.demo700.Services.GymServices;

import java.time.Instant;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.DTOFiles.GymResponse;
import com.example.demo700.Models.GymModels.Gyms;

public interface GymService {
	
	public Gyms addGyms(Gyms gyms, String userId, MultipartFile files[], MultipartFile coverImage);
	public Gyms updateGyms(Gyms gyms, String userId, String gymId, MultipartFile files[], MultipartFile coverImage);
	public List<GymResponse> findByGymTrainer(String gymTrainer);
	public GymResponse findByGymName(String gymName);
	public List<GymResponse> findByLocationName(String locationName);
	public List<GymResponse> findByLatitudeAndLongtitude(double latitude, double longtitude);
	public List<GymResponse> findByEntryFeeLessThanOrEqual(double entryFee);
	public List<GymResponse> findByMonthlyFeeLessThanOrEqual(double monthlyFee);
	public List<GymResponse> seeAllGyms();
	public GymResponse findById(String gymId);
	public boolean deleteGyms(String gymId, String userId);
	public GymResponse findByTradeLicenseId(String tradeLicenseId);
	public GymResponse findByTinNumber(String tinNumber);
	public List<GymResponse> findByOpeningTimeBefore(Instant openingTime);
	public List<GymResponse> findByOpeningTimeAfter(Instant openingTime);
	public List<GymResponse> findByClosingTimeBefore(Instant closingTime);
	public List<GymResponse> findByClosingTimeAfter(Instant closingTime);
	public List<GymResponse> findByGymOwner(String gymOwner);
	public List<GymResponse> findByGymNameContainingIgnoreCase(String gymName);
	
}

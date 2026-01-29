package com.example.demo700.Services;

import java.util.List;

import com.example.demo700.ENUMS.Gender;
import com.example.demo700.Models.User;
import com.example.demo700.Models.UserGender;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.Turf.Owner;

public interface UserGenderService {
	
	public UserGender addUserGender(UserGender userGender);
	public UserGender updateUserGender(UserGender userGender, String userId, String id);
	
	public UserGender findById(String id);
	public List<UserGender> seeAll();
	public UserGender findByUserId(String userId);
	public List<UserGender> findByGender(Gender gender);
	public List<Athelete> findAllAthlete(Gender gender);
	public List<Coach> findAllCoach(Gender gender);
	public List<Scouts> findAllScouts(Gender gender);
	public List<Owner> findAllVenueOwner(Gender gender);
	public List<User> findAllGymTrainer(Gender gender);
	public List<User> findAllGymOwner(Gender gender);
	public List<TeamOwner> findAllTeamOwner(Gender gender);
	
	public boolean deleteUserGender(String id, String userId);

}

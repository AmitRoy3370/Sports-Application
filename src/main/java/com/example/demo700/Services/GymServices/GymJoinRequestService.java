package com.example.demo700.Services.GymServices;

import java.time.Instant;
import java.util.List;

import com.example.demo700.Models.GymModels.GymJoinRequest;
import com.example.demo700.Models.GymModels.GymMember;

public interface GymJoinRequestService {

	public GymJoinRequest addGymJoinRequest(GymJoinRequest gymJoinRequest);

	public GymJoinRequest updayeGymJoinRequest(GymJoinRequest gymJoinRequest, String userId, String gymId);

	public GymJoinRequest findById(String id);

	public List<GymJoinRequest> seeAll();

	public List<GymJoinRequest> findByUserId(String userId);

	public List<GymJoinRequest> findByGymId(String gymId);

	public List<GymJoinRequest> findByRequestSendingTimeBefore(Instant requestSendingTime);

	public List<GymJoinRequest> findByRequestSendingTimeAfter(Instant requestSendingTime);

	public boolean removeGymJoinRequest(String id, String userId);
	
	public GymMember handleGymJoinRequest(String id, String userId, boolean response);
	
	public GymJoinRequest findByGymIdAndUserId(String gymId, String userId);

}

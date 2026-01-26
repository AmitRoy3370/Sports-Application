package com.example.demo700.Repositories.GymRepositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.GymModels.GymJoinRequest;

@Repository
public interface GymJoinRequestRepository extends MongoRepository<GymJoinRequest, String> {

	public List<GymJoinRequest> findByUserId(String userId);
	public List<GymJoinRequest> findByGymId(String gymId);
	public List<GymJoinRequest> findByRequestSendingTimeBefore(Instant requestSendingTime);
	public List<GymJoinRequest> findByRequestSendingTimeAfter(Instant requestSendingTime);
	public GymJoinRequest findByGymIdAndUserId(String gymId, String userId);
}

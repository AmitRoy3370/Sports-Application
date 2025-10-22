package com.example.demo700.Repositories.Athelete;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.ENUMS.TeamJoinRequestStatus;
import com.example.demo700.Models.Athlete.TeamJoinRequest;

@Repository
public interface TeamJoinRequestRepository extends MongoRepository<TeamJoinRequest, String> {
	
	List<TeamJoinRequest> findByReceiverId(String receiverId);
    List<TeamJoinRequest> findBySenderId(String senderId);
    List<TeamJoinRequest> findByTeamId(String teamId);
    List<TeamJoinRequest> findByStatus(TeamJoinRequestStatus status);
    List<TeamJoinRequest> findByPriceGreaterThan(double price);

}

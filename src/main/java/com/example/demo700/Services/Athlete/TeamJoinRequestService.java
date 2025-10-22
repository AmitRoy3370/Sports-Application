package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.ENUMS.AtheletesTeamJoiningResponse;
import com.example.demo700.Models.Athlete.TeamJoinRequest;

public interface TeamJoinRequestService {

	TeamJoinRequest sendJoinRequest(TeamJoinRequest teamJoinRequest, String userId);

	TeamJoinRequest updateRequestStatus(TeamJoinRequest teamJoinRequest, String userId, String teamJoinRequestId);

	List<TeamJoinRequest> getRequestsByReceiver(String receiverId);

	List<TeamJoinRequest> getRequestsBySender(String senderId);

	boolean deleteRequest(String requestId, String userId);

	List<TeamJoinRequest> seeAllTeamJoinRequest();

	List<TeamJoinRequest> searchByPrice(double price);
	
	boolean handleJoinResponse(String teamJoinRequestId, String userId, AtheletesTeamJoiningResponse atheleteResponse);

}

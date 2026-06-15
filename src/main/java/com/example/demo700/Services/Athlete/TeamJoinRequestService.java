package com.example.demo700.Services.Athlete;

import java.util.List;

import com.example.demo700.DTOFiles.TeamJoinRequestResponse;
import com.example.demo700.ENUMS.AtheletesTeamJoiningResponse;
import com.example.demo700.Models.Athlete.TeamJoinRequest;

public interface TeamJoinRequestService {

	TeamJoinRequest sendJoinRequest(TeamJoinRequest teamJoinRequest, String userId);

	TeamJoinRequest updateRequestStatus(TeamJoinRequest teamJoinRequest, String userId, String teamJoinRequestId);

	List<TeamJoinRequestResponse> getRequestsByReceiver(String receiverId);

	List<TeamJoinRequestResponse> getRequestsBySender(String senderId);

	boolean deleteRequest(String requestId, String userId);

	List<TeamJoinRequestResponse> seeAllTeamJoinRequest();

	List<TeamJoinRequestResponse> searchByPrice(double price);
	
	List<TeamJoinRequestResponse> searchByTeamId(String teamId);
	
	boolean handleJoinResponse(String teamJoinRequestId, String userId, AtheletesTeamJoiningResponse atheleteResponse);

}

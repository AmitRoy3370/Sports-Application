package com.example.demo700.Services.GymServices;

import java.util.List;

import com.example.demo700.DTOFiles.GymMemberResponse;
import com.example.demo700.Models.GymModels.GymMember;

public interface GymMemberService {

	public GymMember removeGymMember(String memberId, String gymId, String userId);

	public GymMember addGymMember(String memberId, String gymId, String userId);

	public GymMemberResponse findByGymId(String gymId);
	
	public List<GymMemberResponse> findByGymIdIn(List<String> gymId);

	public List<GymMemberResponse> findByGymMembersContaingingIgnoreCase(String gymMembers);

	public GymMemberResponse findById(String id);

	public List<GymMemberResponse> seeAll();

	public boolean removeGymMember(String id, String userId);

}

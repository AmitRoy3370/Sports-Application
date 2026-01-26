package com.example.demo700.Services.GymServices;

import java.util.List;

import com.example.demo700.Models.GymModels.GymMember;

public interface GymMemberService {

	public GymMember removeGymMember(String memberId, String gymId, String userId);

	public GymMember addGymMember(String memberId, String gymId, String userId);

	public GymMember findByGymId(String gymId);

	public List<GymMember> findByGymMembersContaingingIgnoreCase(String gymMembers);

	public GymMember findById(String id);

	public List<GymMember> seeAll();

	public boolean removeGymMember(String id, String userId);

}

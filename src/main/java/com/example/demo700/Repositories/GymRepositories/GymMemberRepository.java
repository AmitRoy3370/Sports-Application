package com.example.demo700.Repositories.GymRepositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.GymModels.GymMember;

@Repository
public interface GymMemberRepository extends MongoRepository<GymMember, String> {

	public GymMember findByGymId(String gymId);
	public List<GymMember> findByGymMembersContainingIgnoreCase(String gymMembers);
	
}

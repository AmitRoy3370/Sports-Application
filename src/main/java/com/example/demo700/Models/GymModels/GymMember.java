package com.example.demo700.Models.GymModels;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "GymMember")
public class GymMember {

	@Id
	private String id;

	@NonNull
	private String gymId;

	private Set<String> gymMembers;

	public GymMember(String gymId, Set<String> gymMembers) {
		super();
		this.gymId = gymId;
		this.gymMembers = gymMembers;
	}

	public GymMember() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGymId() {
		return gymId;
	}

	public void setGymId(String gymId) {
		this.gymId = gymId;
	}

	public Set<String> getGymMembers() {
		return gymMembers;
	}

	public void setGymMembers(Set<String> gymMembers) {
		this.gymMembers = gymMembers;
	}

	@Override
	public String toString() {
		return "GymMember [id=" + id + ", gymId=" + gymId + ", gymMembers=" + gymMembers + "]";
	}

}

package com.example.demo700.DTOFiles;

import java.util.Set;

public class GymMemberResponse {

	private String id;

	private String gymId, gymName;

	private Set<String> gymMembers, gymMembersName;

	public GymMemberResponse(String id, String gymId, String gymName, Set<String> gymMembers,
			Set<String> gymMembersName) {
		super();
		this.id = id;
		this.gymId = gymId;
		this.gymName = gymName;
		this.gymMembers = gymMembers;
		this.gymMembersName = gymMembersName;
	}

	public GymMemberResponse() {
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

	public String getGymName() {
		return gymName;
	}

	public void setGymName(String gymName) {
		this.gymName = gymName;
	}

	public Set<String> getGymMembers() {
		return gymMembers;
	}

	public void setGymMembers(Set<String> gymMembers) {
		this.gymMembers = gymMembers;
	}

	public Set<String> getGymMembersName() {
		return gymMembersName;
	}

	public void setGymMembersName(Set<String> gymMembersName) {
		this.gymMembersName = gymMembersName;
	}

	@Override
	public String toString() {
		return "GymMemberResponse [id=" + id + ", gymId=" + gymId + ", gymName=" + gymName + ", gymMembers="
				+ gymMembers + ", gymMembersName=" + gymMembersName + "]";
	}

}

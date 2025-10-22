package com.example.demo700.Models.Athlete;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo700.ENUMS.TeamJoinRequestRole;
import com.example.demo700.ENUMS.TeamJoinRequestStatus;
import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "TeamJoinRequest")
@Data
public class TeamJoinRequest {

	@Id
	private String id;

	@NonNull
	private String teamId;

	@NonNull
	private String senderId; // TeamOwner ID

	@NonNull
	private String receiverId; // Athlete / Coach / Scout ID

	@NonNull
	private TeamJoinRequestRole roleType; // "ATHLETE", "COACH", or "SCOUT"

	private Instant requestStartTime;
	private Instant requestEndTime;

	private TeamJoinRequestStatus status;
	
	private double price;

	public TeamJoinRequest() {
		this.requestStartTime = Instant.now();
		this.status = status.ROLE_PENDING;
	}

	public TeamJoinRequest(String teamId, String senderId, String receiverId, TeamJoinRequestRole roleType,
			Instant requestEndTime, double price) {
		super();
		this.teamId = teamId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.roleType = roleType;
		this.requestEndTime = requestEndTime;
		this.requestStartTime = Instant.now();
		this.status = status.ROLE_PENDING;
		this.price = price;
	}

	public TeamJoinRequest(String teamId, String senderId, String receiverId, TeamJoinRequestRole roleType,
			Instant requestEndTime, TeamJoinRequestStatus status, double price) {
		super();
		this.teamId = teamId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.roleType = roleType;
		this.requestEndTime = requestEndTime;
		this.status = status;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public TeamJoinRequestRole getRoleType() {
		return roleType;
	}

	public void setRoleType(TeamJoinRequestRole roleType) {
		this.roleType = roleType;
	}

	public Instant getRequestStartTime() {
		return requestStartTime;
	}

	public void setRequestStartTime(Instant requestStartTime) {
		this.requestStartTime = requestStartTime;
	}

	public Instant getRequestEndTime() {
		return requestEndTime;
	}

	public void setRequestEndTime(Instant requestEndTime) {
		this.requestEndTime = requestEndTime;
	}

	public TeamJoinRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TeamJoinRequestStatus status) {
		this.status = status;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "TeamJoinRequest [id=" + id + ", teamId=" + teamId + ", senderId=" + senderId + ", receiverId="
				+ receiverId + ", roleType=" + roleType + ", requestStartTime=" + requestStartTime + ", requestEndTime="
				+ requestEndTime + ", status=" + status + ", price=" + price + "]";
	}

}

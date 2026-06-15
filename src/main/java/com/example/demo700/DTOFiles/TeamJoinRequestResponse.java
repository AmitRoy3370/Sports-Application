package com.example.demo700.DTOFiles;

import java.time.Instant;

import com.example.demo700.ENUMS.TeamJoinRequestRole;
import com.example.demo700.ENUMS.TeamJoinRequestStatus;

public class TeamJoinRequestResponse {

	private String id;

	private String teamId, teamName;

	private String senderId, senderName; // TeamOwner ID

	private String receiverId; // Athlete / Coach / Scout ID

	private TeamJoinRequestRole roleType; // "ATHLETE", "COACH", or "SCOUT"

	private Instant requestStartTime;
	private Instant requestEndTime;

	private TeamJoinRequestStatus status;

	private double price;

	public TeamJoinRequestResponse(String id, String teamId, String teamName, String senderId, String senderName,
			String receiverId, TeamJoinRequestRole roleType, Instant requestStartTime, Instant requestEndTime,
			TeamJoinRequestStatus status, double price) {
		super();
		this.id = id;
		this.teamId = teamId;
		this.teamName = teamName;
		this.senderId = senderId;
		this.senderName = senderName;
		this.receiverId = receiverId;

		this.roleType = roleType;
		this.requestStartTime = requestStartTime;
		this.requestEndTime = requestEndTime;
		this.status = status;
		this.price = price;
	}

	public TeamJoinRequestResponse() {
		super();
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
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
		return "TeamJoinRequestResponse [id=" + id + ", teamId=" + teamId + ", teamName=" + teamName + ", senderId="
				+ senderId + ", senderName=" + senderName + ", receiverId=" + receiverId + ", roleType=" + roleType
				+ ", requestStartTime=" + requestStartTime + ", requestEndTime=" + requestEndTime + ", status=" + status
				+ ", price=" + price + "]";
	}

}

package com.example.demo700.Models.Turf;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo700.ENUMS.GroupBookingStatus;

import lombok.Data;

@Document(collection = "group_bookings")
@Data
public class GroupBooking {

	@Id
	private String id;
	private String bookingId;
	private List<String> memberIds;
	private double perMemberAmount;
	private GroupBookingStatus status;

	public GroupBooking(String bookingId, List<String> memberIds, double perMemberAmount, String status) {
		super();
		this.bookingId = bookingId;
		this.memberIds = memberIds;
		this.perMemberAmount = perMemberAmount;
		this.status = GroupBookingStatus.valueOf(status);
	}

	public GroupBooking() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public List<String> getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(List<String> memberIds) {
		this.memberIds = memberIds;
	}

	public double getPerMemberAmount() {
		return perMemberAmount;
	}

	public void setPerMemberAmount(double perMemberAmount) {
		this.perMemberAmount = perMemberAmount;
	}

	public GroupBookingStatus getStatus() {
		return status;
	}

	public void setStatus(GroupBookingStatus status) {
		this.status = status;
	}

}

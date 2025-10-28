package com.example.demo700.Models.PaymentGateway;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "BkashTransactions")
@Data
public class BkashTransaction {

	@Id
	private String id;

	private String senderId;
	private String senderName;
	private String senderPhone;

	private String receiverId;
	private String receiverName;
	private String receiverPhone;

	private String bookingId;

	private double amount;
	private String transactionId;

	private Instant transactionTime;

	public BkashTransaction(String senderId, String senderName, String senderPhone, String receiverId,
			String receiverName, String receiverPhone, double amount, String transactionId, String bookingId,
			Instant transactionTime) {
		super();
		this.senderId = senderId;
		this.senderName = senderName;
		this.senderPhone = senderPhone;
		this.receiverId = receiverId;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.amount = amount;
		this.transactionId = transactionId;
		this.transactionTime = transactionTime;
		this.bookingId = bookingId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Instant getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Instant transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	@Override
	public String toString() {
		return "BkashTransaction [id=" + id + ", senderId=" + senderId + ", senderName=" + senderName + ", senderPhone="
				+ senderPhone + ", receiverId=" + receiverId + ", receiverName=" + receiverName + ", receiverPhone="
				+ receiverPhone + ", bookingId=" + bookingId + ", amount=" + amount + ", transactionId=" + transactionId
				+ ", transactionTime=" + transactionTime + "]";
	}

}

package com.example.demo700.DTOFiles;

import java.time.Instant;

public class ChatMessageResponse {

	private String id, senderUserId, receiverId, content;
	private String senderUserName, receiverName;
	private Instant timeStamp;

	public ChatMessageResponse(String id, String senderUserId, String receiverId, String content, String senderUserName,
			String receiverName, Instant timeStamp) {
		super();
		this.id = id;
		this.senderUserId = senderUserId;
		this.receiverId = receiverId;
		this.content = content;
		this.senderUserName = senderUserName;
		this.receiverName = receiverName;
		this.timeStamp = timeStamp;
	}

	public ChatMessageResponse() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderUserName() {
		return senderUserName;
	}

	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Instant getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Instant timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "ChatMessageResponse [id=" + id + ", senderUserId=" + senderUserId + ", receiverId=" + receiverId
				+ ", content=" + content + ", senderUserName=" + senderUserName + ", receiverName=" + receiverName
				+ ", timeStamp=" + timeStamp + "]";
	}

}

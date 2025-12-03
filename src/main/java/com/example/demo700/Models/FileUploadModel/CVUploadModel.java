package com.example.demo700.Models.FileUploadModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;

@Document(collection = "CVInformation")
public class CVUploadModel {

	@Id
	String id;

	@NonNull
	String userId;

	@JsonIgnore
	@NonNull
	String hexFile;

	public CVUploadModel(String userId, String hexFile) {
		super();
		this.userId = userId;
		this.hexFile = hexFile;
	}

	public CVUploadModel() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHexFile() {
		return hexFile;
	}

	public void setHexFile(String hexFile) {
		this.hexFile = hexFile;
	}

	@Override
	public String toString() {
		return "CVUploadModel [id=" + id + ", userId=" + userId + ", hexFile=" + hexFile + "]";
	}

}

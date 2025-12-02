package com.example.demo700.DTOFiles;

public class ProfileImageResponse {

	private String userId;
	private String imageId;
	private String fileName;
	private String contentType;
	private long size;
	private String base64Image; // encoded file

	public ProfileImageResponse(String userId, String imageId, String fileName, String contentType, long size,
			String base64Image) {
		this.userId = userId;
		this.imageId = imageId;
		this.fileName = fileName;
		this.contentType = contentType;
		this.size = size;
		this.base64Image = base64Image;
	}

	public ProfileImageResponse() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	@Override
	public String toString() {
		return "ProfileImageResponse [userId=" + userId + ", imageId=" + imageId + ", fileName=" + fileName
				+ ", contentType=" + contentType + ", size=" + size + ", base64Image=" + base64Image + "]";
	}

}

package com.example.demo700.Models.EventOrganaizer;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "EventOrganaizer")
@Data
public class EventOrganaizer {

	@Id
	String id;

	@NonNull
	String userId;

	@NonNull
	String organaizationName;

	List<String> matches;

	public EventOrganaizer(String userId, String organaizationName, List<String> matches) {
		super();
		this.userId = userId;
		this.organaizationName = organaizationName;
		this.matches = matches;
	}

	public EventOrganaizer() {
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

	public String getOrganaizationName() {
		return organaizationName;
	}

	public void setOrganaizationName(String organaizationName) {
		this.organaizationName = organaizationName;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	@Override
	public String toString() {
		return "EventOrganaizer [id=" + id + ", userId=" + userId + ", organaizationName=" + organaizationName
				+ ", matches=" + matches + "]";
	}

}

package com.example.demo700.Models.Athlete;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "Coach")
@Data
public class Coach {

	@Id
	private String id;

	@NonNull
	private String atheleteId;

	private String teamName;

	private List<String> atheletesVideo;
	private List<String> performanceTracking;

	public Coach(String atheleteId, String teamName, List<String> atheletesVideo, List<String> performanceTracking) {
		super();
		this.atheleteId = atheleteId;
		this.teamName = teamName;
		this.atheletesVideo = atheletesVideo;
		this.performanceTracking = performanceTracking;
	}

	public Coach() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAtheleteId() {
		return atheleteId;
	}

	public void setAtheleteId(String atheleteId) {
		this.atheleteId = atheleteId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<String> getAtheletesVideo() {
		return atheletesVideo;
	}

	public void setAtheletesVideo(List<String> atheletesVideo) {
		this.atheletesVideo = atheletesVideo;
	}

	public List<String> getPerformanceTracking() {
		return performanceTracking;
	}

	public void setPerformanceTracking(List<String> performanceTracking) {
		this.performanceTracking = performanceTracking;
	}

	@Override
	public String toString() {
		return "Coach [id=" + id + ", atheleteId=" + atheleteId + ", teamName=" + teamName + ", atheletesVideo="
				+ atheletesVideo + ", performanceTracking=" + performanceTracking + "]";
	}

}

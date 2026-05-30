package com.example.demo700.Models.Athlete;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "TeamFiles")
public class TeamFiles {

	@Id
	private String id;

	@NonNull
	private String teamId;

	private List<String> files = new ArrayList<>();

	public TeamFiles(String teamId, List<String> files) {
		super();
		this.teamId = teamId;
		this.files = files;
	}

	public TeamFiles() {
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

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "TeamFiles [id=" + id + ", teamId=" + teamId + ", files=" + files + "]";
	}

}

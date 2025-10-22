package com.example.demo700.Models.Athlete;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "Athelete")
@Data
public class Athelete {

	@Id
	private String id;
	@NonNull
	private String userId;
	@NonNull
	private int age, position;
	@NonNull
	private double height, weight;
	private List<String> gameLogs;
	private List<String> eventAttendence;
	private List<String> highlightReels;
	private String presentTeam;

	public Athelete(String userId, int age, int position, double height, double weight, List<String> gameLogs,
			List<String> eventAttendence, List<String> highlightReels, String presentTeam) {
		super();
		this.userId = userId;
		this.age = age;
		this.position = position;
		this.height = height;
		this.weight = weight;
		this.gameLogs = gameLogs;
		this.eventAttendence = eventAttendence;
		this.highlightReels = highlightReels;
		this.presentTeam = presentTeam;
	}

	public Athelete() {
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<String> getGameLogs() {
		return gameLogs;
	}

	public void setGameLogs(List<String> gameLogs) {
		this.gameLogs = gameLogs;
	}

	public List<String> getEventAttendence() {
		return eventAttendence;
	}

	public void setEventAttendence(List<String> eventAttendence) {
		this.eventAttendence = eventAttendence;
	}

	public List<String> getHighlightReels() {
		return highlightReels;
	}

	public void setHighlightReels(List<String> highlightReels) {
		this.highlightReels = highlightReels;
	}

	public String getPresentTeam() {
		return presentTeam;
	}

	public void setPresentTeam(String presentTeam) {
		this.presentTeam = presentTeam;
	}

	@Override
	public String toString() {
		return "Athelete [id=" + id + ", userId=" + userId + ", age=" + age + ", position=" + position + ", height="
				+ height + ", weight=" + weight + ", gameLogs=" + gameLogs + ", eventAttendence=" + eventAttendence
				+ ", highlightReels=" + highlightReels + ", presentTeam=" + presentTeam + "]";
	}

}

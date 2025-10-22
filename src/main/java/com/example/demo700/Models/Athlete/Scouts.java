package com.example.demo700.Models.Athlete;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "Scouts")
@Data
public class Scouts {

	@Id
	private String id;

	@NonNull
	private String atheleteId;

	private List<String> events;
	private List<String> matches;

	public Scouts(String atheleteId, List<String> events, List<String> matches) {
		super();
		this.atheleteId = atheleteId;
		this.events = events;
		this.matches = matches;
	}

	public Scouts() {
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

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	@Override
	public String toString() {
		return "Scouts [id=" + id + ", atheleteId=" + atheleteId + ", events=" + events + ", matches=" + matches + "]";
	}

}

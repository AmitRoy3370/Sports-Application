package com.example.demo700.Models.Turf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "VenueFiles")
public class VenueImages {

	@Id
	private String id;

	@NonNull
	private String venueId;

	private List<String> venueFiles = new ArrayList<>();

	public VenueImages(String venueId, List<String> venueImages) {
		super();
		this.venueId = venueId;
		this.venueFiles = venueImages;
	}

	public VenueImages() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public List<String> getVenueFiles() {
		return venueFiles;
	}

	public void setVenueFiles(List<String> venueImages) {
		this.venueFiles = venueImages;
	}

	@Override
	public String toString() {
		return "VenueImages [id=" + id + ", venueId=" + venueId + ", venueFiles=" + venueFiles + "]";
	}

}

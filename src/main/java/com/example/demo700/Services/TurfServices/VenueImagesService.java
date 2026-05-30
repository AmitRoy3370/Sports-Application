package com.example.demo700.Services.TurfServices;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.Models.Turf.VenueImages;

public interface VenueImagesService {

	public VenueImages addVenueImages(MultipartFile file[], String venueId, String userId);
	public VenueImages updateVenueImages(MultipartFile file[], String venueId, String userId, String venueImagesId, List<String> existingFiles);
	
	public List<VenueImages> seeAll();
	public VenueImages findByVenueId(String venueId);
	public List<VenueImages> findByVenueFilesContainingIgnoreCase(String fileId);
	
	public boolean deleteVenueImages(String userId, String venueImagesId);
	
}

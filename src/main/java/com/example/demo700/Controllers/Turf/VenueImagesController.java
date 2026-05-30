package com.example.demo700.Controllers.Turf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.Models.Turf.VenueImages;
import com.example.demo700.Services.FileUploadServices.ImageService;
import com.example.demo700.Services.TurfServices.VenueImagesService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.gridfs.model.GridFSFile;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api/venue-images")
public class VenueImagesController {

	@Autowired
	private VenueImagesService venueImagesService;

	@Autowired
	private ImageService imageService;

	/**
	 * Add venue images Endpoint: POST /api/venue-images/add Content-Type:
	 * multipart/form-data
	 * 
	 * Parameters: - files: Array of image files (multipart files) - venueId: Venue
	 * ID (form parameter) - userId: User ID (form parameter)
	 */
	@PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addVenueImages(@RequestPart(value = "files", required = true) MultipartFile[] files,
			@RequestParam("venueId") String venueId, @RequestParam("userId") String userId) {

		try {
			// Validate input
			if (files == null || files.length == 0) {
				return ResponseEntity.badRequest().body("At least one image file is required");
			}

			if (venueId == null || venueId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Venue ID is required");
			}

			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("User ID is required");
			}

			VenueImages result = venueImagesService.addVenueImages(files, venueId, userId);

			return ResponseEntity.status(HttpStatus.CREATED).body(result);

		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to add venue images: " + e.getMessage());
		}
	}

	/**
	 * Update venue images Endpoint: PUT /api/venue-images/update Content-Type:
	 * multipart/form-data
	 * 
	 * Parameters: - files: New image files to add (multipart files, optional) -
	 * venueId: Venue ID (form parameter) - userId: User ID (form parameter) -
	 * venueImagesId: Venue images document ID (form parameter) - existingFiles:
	 * List of existing file IDs to keep (form parameter, optional)
	 */
	@PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateVenueImagesJson(
			@RequestPart(value = "files", required = false) MultipartFile[] files,
			@RequestParam("venueId") String venueId, @RequestParam("userId") String userId,
			@RequestParam("venueImagesId") String venueImagesId,
			@RequestParam(value = "existingFilesJson", required = false) String existingFilesJson) {

		try {
			// Validate required inputs
			if (venueId == null || venueId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Venue ID is required");
			}

			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("User ID is required");
			}

			if (venueImagesId == null || venueImagesId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Venue Images ID is required");
			}

			// Parse existingFiles from JSON array string
			List<String> existingFiles = new ArrayList<>();

			// Parse existingFiles from comma-separated string
			if (existingFilesJson != null && !existingFilesJson.isBlank()) {

				ObjectMapper mapper = new ObjectMapper();
				existingFiles = mapper.readValue(existingFilesJson, new TypeReference<List<String>>() {
				});
				// gyms.setGymImages(existingFiles);

			}

			// Handle null files
			MultipartFile[] fileArray = (files != null) ? files : new MultipartFile[0];

			VenueImages result = venueImagesService.updateVenueImages(fileArray, venueId, userId, venueImagesId,
					existingFiles);

			return ResponseEntity.ok().body(result);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update venue images: " + e.getMessage());
		}
	}

	/**
	 * Get all venue images Endpoint: GET /api/venue-images/all
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllVenueImages() {
		try {
			List<VenueImages> result = venueImagesService.seeAll();
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	/**
	 * Get venue images by venue ID Endpoint: GET /api/venue-images/venue/{venueId}
	 */
	@GetMapping("/venue/{venueId}")
	public ResponseEntity<?> getVenueImagesByVenueId(@PathVariable String venueId) {
		try {
			VenueImages result = venueImagesService.findByVenueId(venueId);
			return ResponseEntity.ok(result);
		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	/**
	 * Get venue images by file ID Endpoint: GET /api/venue-images/file/{fileId}
	 */
	@GetMapping("/file/{fileId}")
	public ResponseEntity<?> getVenueImagesByFileId(@PathVariable String fileId) {
		try {
			List<VenueImages> result = venueImagesService.findByVenueFilesContainingIgnoreCase(fileId);
			return ResponseEntity.ok(result);
		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// -------------- view the attachment ---------------------

	@GetMapping("/attachment/view/{attachmentId}")
	public ResponseEntity<?> viewAttachment(@PathVariable String attachmentId) {
		try {
			GridFSFile file = imageService.getFile(attachmentId);

			if (file == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
			}

			InputStream stream = imageService.getStream(file);

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getMetadata().get("type").toString()))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
					.body(new InputStreamResource(stream));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load file");
		}
	}

	// ------------------- download attachment -----------------

	@GetMapping("/attachment/{attachmentId}")
	public ResponseEntity<?> downloadAttachment(@PathVariable String attachmentId) {

		try {
			GridFSFile file = imageService.getFile(attachmentId);

			if (file == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
			}

			InputStream stream = imageService.getStream(file);

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getMetadata().get("type").toString()))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(new InputStreamResource(stream));

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download image");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download image");
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download image");
		}

	}

	/**
	 * Delete venue images Endpoint: DELETE /api/venue-images/delete/{venueImagesId}
	 * Query parameter: userId
	 */
	@DeleteMapping("/delete/{venueImagesId}")
	public ResponseEntity<?> deleteVenueImages(@PathVariable String venueImagesId, @RequestParam String userId) {

		try {
			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("User ID is required");
			}

			boolean result = venueImagesService.deleteVenueImages(userId, venueImagesId);

			if (result) {
				return ResponseEntity.ok("Venue images deleted successfully");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete venue images");
			}

		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete venue images: " + e.getMessage());
		}
	}
}

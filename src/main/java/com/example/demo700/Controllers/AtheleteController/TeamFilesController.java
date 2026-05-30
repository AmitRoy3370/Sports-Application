package com.example.demo700.Controllers.AtheleteController;

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

import com.example.demo700.Models.Athlete.TeamFiles;
import com.example.demo700.Services.Athlete.TeamFilesService;
import com.example.demo700.Services.FileUploadServices.ImageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.gridfs.model.GridFSFile;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api/team-files")
public class TeamFilesController {

	@Autowired
	private TeamFilesService teamFilesService;

	@Autowired
	private ImageService imageService;

	/**
	 * Add team files Endpoint: POST /api/team-files/add Content-Type:
	 * multipart/form-data
	 * 
	 * Parameters: - files: Array of files (multipart files) - userId: User ID (form
	 * parameter) - teamId: Team ID (form parameter)
	 */
	@PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addTeamFiles(@RequestPart(value = "files") MultipartFile[] files,
			@RequestParam("userId") String userId, @RequestParam("teamId") String teamId) {

		try {
			// Validate input
			if (files == null || files.length == 0) {
				return ResponseEntity.badRequest().body("At least one file is required");
			}

			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("User ID is required");
			}

			if (teamId == null || teamId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Team ID is required");
			}

			TeamFiles result = teamFilesService.addTeamFiles(files, userId, teamId);

			return ResponseEntity.status(HttpStatus.CREATED).body(result);

		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to add team files: " + e.getMessage());
		}
	}

	/**
	 * Update team files Endpoint: PUT /api/team-files/update Content-Type:
	 * multipart/form-data
	 * 
	 * Parameters: - files: New files to add (optional) - userId: User ID (form
	 * parameter) - teamFilesId: Team files document ID (form parameter) -
	 * existingFiles: Comma-separated list of existing file IDs to keep (optional) -
	 * teamId: Team ID (form parameter)
	 */
	@PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateTeamFiles(@RequestPart(value = "files", required = false) MultipartFile[] files,
			@RequestParam("userId") String userId, @RequestParam("teamFilesId") String teamFilesId,
			@RequestParam(value = "existingFiles", required = false) String existingFilesJson,
			@RequestParam("teamId") String teamId) {

		try {
			// Validate required inputs
			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("User ID is required");
			}

			if (teamFilesId == null || teamFilesId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Team Files ID is required");
			}

			if (teamId == null || teamId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Team ID is required");
			}

			List<String> existingFiles = new ArrayList<>();
			
			// Parse existingFiles from comma-separated string
			if (existingFilesJson != null && !existingFilesJson.isBlank()) {

				ObjectMapper mapper = new ObjectMapper();
	            existingFiles = mapper.readValue(
	            		existingFilesJson,
	                    new TypeReference<List<String>>() {}
	            );
	            //gyms.setGymImages(existingFiles);

			}

			// Handle null files
			MultipartFile[] fileArray = (files != null) ? files : new MultipartFile[0];
			
			TeamFiles result = teamFilesService.updateTeamFiles(fileArray, userId, teamFilesId, existingFiles,
					teamId);

			return ResponseEntity.ok().body(result);

		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update team files: " + e.getMessage());
		}
	}

	/**
	 * Update team files with JSON array for existing files Endpoint: PUT
	 * /api/team-files/update/json Content-Type: multipart/form-data
	 */
	@PutMapping(value = "/update/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateTeamFilesJson(@RequestPart(value = "files", required = false) MultipartFile[] files,
			@RequestParam("userId") String userId, @RequestParam("teamFilesId") String teamFilesId,
			@RequestParam(value = "existingFilesJson", required = false) String existingFilesJson,
			@RequestParam("teamId") String teamId) {

		try {
			// Validate required inputs
			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("User ID is required");
			}

			if (teamFilesId == null || teamFilesId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Team Files ID is required");
			}

			if (teamId == null || teamId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Team ID is required");
			}

			// Parse existingFiles from JSON array string
			List<String> existingFileList = null;
			if (existingFilesJson != null && !existingFilesJson.trim().isEmpty()) {
				// Remove brackets and split by comma
				String cleaned = existingFilesJson.replace("[", "").replace("]", "").replace("\"", "");
				if (!cleaned.trim().isEmpty()) {
					existingFileList = List.of(cleaned.split(","));
				}
			}

			// Handle null files
			MultipartFile[] fileArray = (files != null) ? files : new MultipartFile[0];
			List<String> existingFileListSafe = (existingFileList != null) ? existingFileList : List.of();

			TeamFiles result = teamFilesService.updateTeamFiles(fileArray, userId, teamFilesId, existingFileListSafe,
					teamId);

			return ResponseEntity.ok().body(result);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update team files: " + e.getMessage());
		}
	}

	/**
	 * Get all team files Endpoint: GET /api/team-files/all
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllTeamFiles() {
		try {
			List<TeamFiles> result = teamFilesService.seeAll();
			return ResponseEntity.ok(result);
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
	 * Get team files by team ID Endpoint: GET /api/team-files/team/{teamId}
	 */
	@GetMapping("/team/{teamId}")
	public ResponseEntity<?> getTeamFilesByTeamId(@PathVariable String teamId) {
		try {
			if (teamId == null || teamId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Team ID is required");
			}

			TeamFiles result = teamFilesService.findByTeamId(teamId);
			return ResponseEntity.ok(result);

		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	/**
	 * Get team files by file ID Endpoint: GET /api/team-files/file/{fileId}
	 */
	@GetMapping("/file/{fileId}")
	public ResponseEntity<?> getTeamFilesByFileId(@PathVariable String fileId) {
		try {
			if (fileId == null || fileId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("File ID is required");
			}

			List<TeamFiles> result = teamFilesService.findByFilesContainingIgnoreCase(fileId);
			return ResponseEntity.ok(result);

		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	/**
	 * Delete team files Endpoint: DELETE /api/team-files/delete/{teamFilesId} Query
	 * parameter: userId
	 */
	@DeleteMapping("/delete/{teamFilesId}")
	public ResponseEntity<?> deleteTeamFiles(@PathVariable String teamFilesId, @RequestParam String userId) {

		try {
			if (teamFilesId == null || teamFilesId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Team Files ID is required");
			}

			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("User ID is required");
			}

			boolean result = teamFilesService.removeTeamFiles(teamFilesId, userId);

			if (result) {
				return ResponseEntity.ok("Team files deleted successfully");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete team files");
			}

		} catch (NullPointerException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete team files: " + e.getMessage());
		}
	}
}

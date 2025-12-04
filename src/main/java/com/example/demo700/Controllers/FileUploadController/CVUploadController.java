package com.example.demo700.Controllers.FileUploadController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.FileUploadModel.CVUploadModel;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.FileUploadRepositories.CVUploadRepository;
import com.example.demo700.Services.FileUploadServices.CVUploadService;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
public class CVUploadController {

	@Autowired
	private CVUploadRepository cvRepo;

	@Autowired
	private CVUploadService cvService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CyclicCleaner cleaner;

	// ============================
	// ✔ CREATE / UPLOAD CV
	// ============================
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadCv(@RequestPart("userId") String userId, @RequestPart("file") MultipartFile file) {

		try {

			try {

				User user = userRepository.findById(userId).get();

				if (user == null) {

					throw new Exception("No such user exist at here...");

				}

			} catch (Exception e) {

				return ResponseEntity.status(400).body(e.getMessage());

			}

			String hexData = cvService.uploadCv(file);

			CVUploadModel model = cvRepo.findByUserId(userId);
			if (model != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("CV already uploaded for this user.");
			}

			CVUploadModel saveObj = new CVUploadModel(userId, hexData);
			cvRepo.save(saveObj);

			return ResponseEntity.ok("CV uploaded successfully!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Upload failed: " + e.getMessage());
		}
	}

	// ============================
	// ✔ DOWNLOAD CV
	// ============================
	@GetMapping("/download/{userId}")
	public ResponseEntity<?> downloadCv(
	        @PathVariable String userId,
	        @RequestParam(defaultValue = "view") String mode) {

	    CVUploadModel model = cvRepo.findByUserId(userId);
	    if (model == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("CV not found for this user.");
	    }

	    byte[] fileBytes = cvService.getCv(model.getHexFile());
	    
	    User user = userRepository.findById(userId).get();
	    
	    String fileName = user.getName();

	    MediaType mediaType = getMediaType(fileName);

	    String disposition = mode.equalsIgnoreCase("download")
	            ? "attachment"
	            : "inline";

	    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
				.contentType(mediaType).body(fileBytes);
	}



	// ============================
	// ✔ READ CV BY USER ID
	// ============================
	@GetMapping("/get/{userId}")
	public ResponseEntity<?> getCvHex(@PathVariable String userId) {

		CVUploadModel model = cvRepo.findByUserId(userId);
		if (model == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found for this user.");
		}

		return ResponseEntity.ok(model);
	}

	// ============================
	// ✔ UPDATE CV
	// ============================
	@PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateCv(@RequestPart("userId") String userId, @RequestPart("file") MultipartFile file,
			@RequestPart("CVInformationId") String cvInformationId, @RequestParam String userTryingToUpdate) {

		try {

			try {

				User user = userRepository.findById(userTryingToUpdate).get();

				if (user == null) {

					throw new Exception("No such user exist at here...");

				}

				if (!user.getId().equals(userId)) {

					throw new Exception("You can only update yourself...");

				}

			} catch (Exception e) {

				return ResponseEntity.status(400).body(e.getMessage());

			}

			CVUploadModel model = cvRepo.findByUserId(userId);
			if (model == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existing CV found for update.");
			}

			if (!model.getUserId().equals(userId)) {

				return ResponseEntity.status(400).body("You can only update yourself...");

			}

			String newHex = cvService.updateCv(file);
			model.setHexFile(newHex);
			cvRepo.save(model);

			return ResponseEntity.ok("CV updated successfully!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed: " + e.getMessage());
		}
	}

	// ============================
	// ✔ DELETE CV
	// ============================
	@DeleteMapping("/delete/{userId}/{cvId}")
	public ResponseEntity<?> deleteCv(@PathVariable String userId, @PathVariable String cvId) {

		try {

			CVUploadModel model = cvRepo.findById(cvId).get();

			if (model == null) {

				throw new Exception("No such cv uploaded at here...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception("No such user find at here...");

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = cvRepo.count();

				cleaner.removeCV(cvId);

				return count != cvRepo.count() ? ResponseEntity.ok("CV deleted successfully!")
						: ResponseEntity.status(500).body("CV is not deleted...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		CVUploadModel model = cvRepo.findByUserId(userId);
		if (model == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found for this user.");
		}

		if (!model.getUserId().equals(userId) || !model.getId().equals(cvId)) {

			return ResponseEntity.status(400).body("You only can remove your cv...");

		}

		long count = cvRepo.count();

		cleaner.removeCV(model.getId());

		return count != cvRepo.count() ? ResponseEntity.ok("CV deleted successfully!")
				: ResponseEntity.status(500).body("CV is not deleted...");
	}

	// ============================
	// ✔ GET ALL CV ENTRIES
	// ============================
	@GetMapping("/all")
	public ResponseEntity<?> getAllCvs() {
		List<CVUploadModel> list = cvRepo.findAll();
		return ResponseEntity.ok(list);
	}

	// ============================
	// ✔ FILTER BY USER ID (partial)
	// Example: /api/cv/search?userId=arpon
	// ============================
	@GetMapping("/search")
	public ResponseEntity<?> searchCv(@RequestParam("userId") String userId) {

		List<CVUploadModel> list = cvRepo.findAll().stream()
				.filter(x -> x.getUserId().toLowerCase().contains(userId.toLowerCase())).toList();

		if (list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No CV found matching the filter.");
		}

		return ResponseEntity.ok(list);
	}
	
	private MediaType getMediaType(String fileName) {

	    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

	    switch (extension) {
	        case "pdf":
	            return MediaType.APPLICATION_PDF;

	        case "jpg":
	        case "jpeg":
	            return MediaType.IMAGE_JPEG;

	        case "png":
	            return MediaType.IMAGE_PNG;

	        case "doc":
	            return MediaType.parseMediaType("application/msword");

	        case "docx":
	            return MediaType.parseMediaType(
	                "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

	        case "mp4":
	            return MediaType.parseMediaType("video/mp4");

	        case "txt":
	            return MediaType.TEXT_PLAIN;

	        default:
	            return MediaType.APPLICATION_OCTET_STREAM;
	    }
	}

}

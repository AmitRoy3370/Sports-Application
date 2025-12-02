package com.example.demo700.Controllers.FileUploadController;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import com.example.demo700.DTOFiles.ProfileImageResponse;
import com.example.demo700.Models.User;
import com.example.demo700.Models.FileUploadModel.ProfileIamge;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;
import com.example.demo700.Services.FileUploadServices.ImageService;

import com.mongodb.client.gridfs.model.GridFSFile;

@RestController
@RequestMapping("/api/profileIamge")
public class ProfileImageController {

	@Autowired
	private ImageService imageService;

	@Autowired
	private ProfileImageRepository profileImageRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<?> uploadProfileIamge(@RequestParam String requestedUserId,
			@RequestPart("userId") String userId, @RequestPart("file") MultipartFile file) {

		if (requestedUserId == null || userId == null || file == null || file.isEmpty()) {

			return ResponseEntity.status(400).body("False request...");

		}

		if (!userId.equals(requestedUserId)) {

			return ResponseEntity.status(400).body("You can upload only your profile image");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body("No such user exist at here...");

		}

		try {

			ProfileIamge profileImage = profileImageRepository.findByUserId(userId);

			if (profileImage != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			return ResponseEntity.status(400).body("You already add your profile image, now you can update it only...");

		} catch (Exception e) {

		}

		try {

			String imageHex = null;

			String contentType = file.getContentType();

			if (contentType != null && contentType.startsWith("image/")) {

				String hexCode = imageService.upload(file);

				if (hexCode != null) {

					imageHex = hexCode;

				}

			}

			ProfileIamge profileImage = new ProfileIamge(userId, imageHex);

			profileImage = profileImageRepository.save(profileImage);

			if (profileImage == null) {

				throw new Exception("Your profile image is no uploaded...");

			}

			GridFSFile uploadedFile = imageService.getFile(imageHex);

			byte[] fileBytes = imageService.getStream(uploadedFile).readAllBytes();
			String base64 = Base64.getEncoder().encodeToString(fileBytes);

			ProfileImageResponse response = new ProfileImageResponse(userId, imageHex, uploadedFile.getFilename(),
					uploadedFile.getMetadata().getString("type"), uploadedFile.getLength(), base64);

			return ResponseEntity.status(201).body(response);

		} catch (Exception e) {

			return ResponseEntity.status(500).body(e.getMessage());

		}

	}

	@PutMapping(value = "/updateProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<?> updateProfileIamge(@RequestParam String requestedUserId,
			@RequestPart("userId") String userId, @RequestPart("file") MultipartFile file,
			@RequestPart("profileImageId") String profileImageId) {

		if (requestedUserId == null || userId == null || file == null || file.isEmpty()) {

			return ResponseEntity.status(400).body("False request...");

		}

		if (!userId.equals(requestedUserId)) {

			return ResponseEntity.status(400).body("You can upload only your profile image");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body("No such user exist at here...");

		}

		try {

			ProfileIamge profileImage = profileImageRepository.findByUserId(userId);

			if (profileImage != null) {

				if (!profileImage.getId().equals(profileImageId)) {

					if (!profileImage.getId().equals(profileImageId)) {

						throw new ArithmeticException();

					}

				}

			}

		} catch (ArithmeticException e) {

			return ResponseEntity.status(400).body("You can't update other's profile image...");

		} catch (Exception e) {

		}

		ProfileIamge existingProfileImage = profileImageRepository.findById(profileImageId).get();

		try {

			String imageHex = null;

			String contentType = file.getContentType();

			if (contentType != null && contentType.startsWith("image/")) {

				if (imageService.getFile(existingProfileImage.getImageHex()) == null) {

					String hexCode = imageService.upload(file);

					if (hexCode != null) {

						imageHex = hexCode;

					}

				} else {

					String hexCode = imageService.update(existingProfileImage.getImageHex(), file);

					if (hexCode != null) {

						imageHex = hexCode;

					}

				}

			}

			ProfileIamge profileImage = new ProfileIamge(userId, imageHex);

			profileImage.setId(profileImageId);

			profileImage = profileImageRepository.save(profileImage);

			if (profileImage == null) {

				throw new Exception("Your profile image is no uploaded...");

			}

			GridFSFile uploadedFile = imageService.getFile(imageHex);

			byte[] fileBytes = imageService.getStream(uploadedFile).readAllBytes();
			String base64 = Base64.getEncoder().encodeToString(fileBytes);

			ProfileImageResponse response = new ProfileImageResponse(userId, imageHex, uploadedFile.getFilename(),
					uploadedFile.getMetadata().getString("type"), uploadedFile.getLength(), base64);

			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {

			return ResponseEntity.status(500).body(e.getMessage());

		}

	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> download(@PathVariable String id) {
		try {
			GridFSFile file = imageService.getFile(id);
			if (file == null) {
				return ResponseEntity.status(404).body("File not found");
			}

			byte[] bytes = imageService.getStream(file).readAllBytes();

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getMetadata().getString("type")))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename()).body(bytes);

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error downloading file");
		}
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteProfileImage(@PathVariable String userId) {
		try {
			ProfileIamge profileImage = profileImageRepository.findByUserId(userId);
			if (profileImage == null) {
				return ResponseEntity.status(404).body("No image found");
			}

			imageService.delete(profileImage.getImageHex());
			profileImageRepository.deleteById(profileImage.getId());

			return ResponseEntity.ok("Image deleted successfully");

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error deleting file");
		}
	}

}

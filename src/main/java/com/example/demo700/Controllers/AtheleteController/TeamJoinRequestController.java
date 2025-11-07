package com.example.demo700.Controllers.AtheleteController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.Athlete.TeamJoinRequest;
import com.example.demo700.Services.Athlete.TeamJoinRequestService;

import jakarta.jws.soap.InitParam;

import com.example.demo700.ENUMS.AtheletesTeamJoiningResponse;
import com.example.demo700.ENUMS.TeamJoinRequestStatus;

@RestController
@RequestMapping("/api/teamJoinRequests")
public class TeamJoinRequestController {

	@Autowired
	private TeamJoinRequestService teamJoinRequestService;

	// ✅ 1️⃣ Send a new team join request
	@PostMapping("/send/{userId}")
	public ResponseEntity<?> sendJoinRequest(@RequestBody TeamJoinRequest teamJoinRequest,
			@PathVariable String userId) {
		try {
			TeamJoinRequest savedRequest = teamJoinRequestService.sendJoinRequest(teamJoinRequest, userId);
			return ResponseEntity.ok(savedRequest);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error sending join request: " + e.getMessage());
		}
	}

	// ✅ 2️⃣ Update the status or details of a request
	@PutMapping("/update/{userId}/{requestId}")
	public ResponseEntity<?> updateRequestStatus(@RequestBody TeamJoinRequest teamJoinRequest,
			@PathVariable String userId, @PathVariable String requestId) {
		try {
			TeamJoinRequest updatedRequest = teamJoinRequestService.updateRequestStatus(teamJoinRequest, userId,
					requestId);
			return ResponseEntity.ok(updatedRequest);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating join request: " + e.getMessage());
		}
	}

	@PutMapping("/handleResponse")
	public ResponseEntity<?> handleJoinResponse(@RequestParam String teamJoinRequestId, @RequestParam String userId,
			@RequestParam String atheleteResponse) {

		try {

			boolean yes = teamJoinRequestService.handleJoinResponse(teamJoinRequestId, userId,
					AtheletesTeamJoiningResponse.valueOf(atheleteResponse));

			if (!yes) {

				return ResponseEntity.status(500).body("Team member is not added...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

		return ResponseEntity.status(200).body("Team member added...");

	}

	// ✅ 3️⃣ Get all requests received by a user (Receiver)
	@GetMapping("/receiver/{receiverId}")
	public ResponseEntity<?> getRequestsByReceiver(@PathVariable String receiverId) {
		try {
			List<TeamJoinRequest> requests = teamJoinRequestService.getRequestsByReceiver(receiverId);
			return ResponseEntity.ok(requests);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching receiver requests: " + e.getMessage());
		}
	}

	// ✅ 4️⃣ Get all requests sent by a user (Sender)
	@GetMapping("/sender/{senderId}")
	public ResponseEntity<?> getRequestsBySender(@PathVariable String senderId) {
		try {
			List<TeamJoinRequest> requests = teamJoinRequestService.getRequestsBySender(senderId);
			return ResponseEntity.ok(requests);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching sender requests: " + e.getMessage());
		}
	}

	// ✅ 5️⃣ Delete a request
	@DeleteMapping("/delete/{requestId}/{userId}")
	public ResponseEntity<?> deleteRequest(@PathVariable String requestId, @PathVariable String userId) {
		try {
			boolean deleted = teamJoinRequestService.deleteRequest(requestId, userId);
			if (deleted) {
				return ResponseEntity.ok("Team join request deleted successfully.");
			} else {
				return ResponseEntity.badRequest().body("Failed to delete team join request.");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting join request: " + e.getMessage());
		}
	}

	// ✅ 6️⃣ Get all team join requests (Admin or debug purpose)
	@GetMapping("/all")
	public ResponseEntity<?> seeAllTeamJoinRequest() {
		try {
			List<TeamJoinRequest> requests = teamJoinRequestService.seeAllTeamJoinRequest();
			return ResponseEntity.ok(requests);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching all join requests: " + e.getMessage());
		}
	}

	// ✅ 7️⃣ Optional: Get requests by status (PENDING / APPROVED / REJECTED)
	@GetMapping("/status/{status}")
	public ResponseEntity<?> getRequestsByStatus(@PathVariable TeamJoinRequestStatus status) {
		try {
			List<TeamJoinRequest> requests = teamJoinRequestService.seeAllTeamJoinRequest().stream()
					.filter(r -> r.getStatus() == status).toList();
			return ResponseEntity.ok(requests);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching by status: " + e.getMessage());
		}
	}

	@GetMapping("/searchByPrice")
	public ResponseEntity<?> getRequestByPrice(@RequestParam String price) {

		try {

			List<TeamJoinRequest> list = teamJoinRequestService.searchByPrice(Double.parseDouble(price));

			return ResponseEntity.status(200).body(list);

		} catch (Exception e) {

			return ResponseEntity.status(400).body("In valid price...");

		}

	}

	@GetMapping("/searchByTeamId")
	public ResponseEntity<?> getByTeamId(@RequestParam String teamId) {

		try {

			List<TeamJoinRequest> list = teamJoinRequestService.searchByTeamId(teamId);

			if (list.isEmpty()) {

				return ResponseEntity.status(404).body("No team find at here...");

			}

			return ResponseEntity.status(200).body(list);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

}

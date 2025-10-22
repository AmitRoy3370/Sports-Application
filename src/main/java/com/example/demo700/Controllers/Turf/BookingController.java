package com.example.demo700.Controllers.Turf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.DTOFiles.BookingRequestDto;
import com.example.demo700.Models.Turf.Booking;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Services.TurfServices.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/addBooking")
	public ResponseEntity<?> create(@Validated @RequestBody BookingRequestDto dto) {

		Booking b = null;

		try {

			b = bookingService.createBooking(dto);

			if (b == null) {

				return ResponseEntity.status(500).body("Booking is not added at here...");

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body("Invalid input data");

		}

		return ResponseEntity.status(201).body(b);
	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAll() {

		List<Booking> list = bookingService.seeAll();

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("No Booking find at here....");

		} else {

			return ResponseEntity.status(200).body(list);

		}

	}

	@PutMapping("/updateBooking")
	public ResponseEntity<?> updateBooking(@RequestParam String id, @RequestParam String userId,
			@RequestBody BookingRequestDto dto) {

		try {

			if (!userId.equals(dto.getUserId())) {

				return ResponseEntity.status(400).body("Un wanted access find....");

			}

			Booking updatedBooking = bookingService.updateBooking(id, dto);

			if (updatedBooking == null) {

				return ResponseEntity.status(400).body("Please input all the data correctly...");

			}

			return ResponseEntity.status(200).body(updatedBooking);

		} catch (Exception e) {

			return ResponseEntity.status(400).body("Please input the valid data");

		}

	}

	@PutMapping("/updateStatus")
	public ResponseEntity<?> updateBookingStatus(@RequestParam String bookingId, @RequestParam String ownerId, @RequestParam String status) {

		try {

			Booking booking = bookingService.updateBookingStatus(bookingId, ownerId, status);

			if (booking == null) {

				return ResponseEntity.status(400).body("Status not updated...");

			} else {

				return ResponseEntity.status(200).body(booking);

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}

	}

	@DeleteMapping("/deleteBooking")
	public ResponseEntity<?> deleteBooking(@RequestParam() String id, @RequestParam() String isGroup,
			@RequestParam("givenUserId") String givenUserId) {

		try {

			Boolean.parseBoolean(isGroup);

		} catch (Exception e) {

			return ResponseEntity.status(400).body("Invalid input");

		}

		boolean yes = bookingService.deleteBooking(id, Boolean.parseBoolean(isGroup), givenUserId);

		if (yes) {

			return ResponseEntity.status(200).body("Booking deleted");

		} else {

			return ResponseEntity.status(500).body("Booking is not deleted");

		}

	}

}
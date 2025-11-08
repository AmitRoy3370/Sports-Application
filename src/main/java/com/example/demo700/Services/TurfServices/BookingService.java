package com.example.demo700.Services.TurfServices;

import java.util.List;

import com.example.demo700.DTOFiles.BookingRequestDto;
import com.example.demo700.Models.Turf.Booking;

public interface BookingService {
	Booking createBooking(BookingRequestDto request);

	List<Booking> seeAll();

	Booking updateBooking(String id, BookingRequestDto request);

	boolean deleteBooking(String id, boolean isGroup, String userId);

	Booking updateBookingStatus(String bookingId, String ownerId, String status);

	List<Booking> findByVenueIdAndStatus(String venueId, com.example.demo700.ENUMS.BookingStatus status);

	List<Booking> findByUserId(String userId);

	List<Booking> findByVenueId(String venueId);
}

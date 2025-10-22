package com.example.demo700.Repositories.Turf;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

	List<Booking> findByVenueIdAndEndTimeAfterAndStartTimeBefore(String venueId, Instant startExclusive,
			Instant endExclusive);

	List<Booking> findByVenueIdAndStatus(String venueId, com.example.demo700.ENUMS.BookingStatus status);

	List<Booking> findByUserId(String userId);
	
}

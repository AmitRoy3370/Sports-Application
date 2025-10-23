package com.example.demo700.Repositories.Turf;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.GroupBooking;

@Repository
public interface GroupBookingRepository extends MongoRepository<GroupBooking, String> {
	
	List<GroupBooking> findByMemberIdsContainingIgnoreCase(String userId);
	List<GroupBooking> findByBookingId(String bookingId);
	

}

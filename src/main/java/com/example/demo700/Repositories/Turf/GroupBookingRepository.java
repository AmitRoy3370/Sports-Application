package com.example.demo700.Repositories.Turf;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Turf.GroupBooking;

@Repository
public interface GroupBookingRepository extends MongoRepository<GroupBooking, String> {

}

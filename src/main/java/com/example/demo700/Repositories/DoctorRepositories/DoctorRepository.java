package com.example.demo700.Repositories.DoctorRepositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.DoctorModels.Doctor;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

	Doctor findByUserId(String userId);
	List<Doctor> findByYearOfExperiencesGreaterThan(int year);
	List<Doctor> findByDesignationIgnoreCase(String designation);
	List<Doctor> findByDegressContainingIgnoreCase(String degress);
	List<Doctor> findByWorkingExperiencesContainingIgnoreCase(String workingExperiences);
	
}

package com.example.demo700.Services.DoctorServices;

import java.util.List;

import com.example.demo700.Models.DoctorModels.Doctor;

public interface DoctorService {
	
	public Doctor addDoctor(String userId, Doctor doctor);
	public List<Doctor> seeAllDoctor();
	Doctor findByUserId(String userId);
	List<Doctor> findByYearOfExperiencesGreaterThan(int year);
	List<Doctor> findByDesignationIgnoreCase(String designation);
	List<Doctor> findByDegressContainingIgnoreCase(String degress);
	List<Doctor> findByWorkingExperiencesContainingIgnoreCase(String workingExperiences);
	Doctor findById(String doctorId);
	public Doctor updateDoctor(String userId, String doctorId, Doctor updatedDoctor);
	public boolean deleteDoctor(String userId, String doctorId);

}

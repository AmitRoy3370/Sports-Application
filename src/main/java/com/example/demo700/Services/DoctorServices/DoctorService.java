package com.example.demo700.Services.DoctorServices;

import java.util.List;

import com.example.demo700.DTOFiles.DoctorResponse;
import com.example.demo700.Models.DoctorModels.Doctor;

public interface DoctorService {
	
	public Doctor addDoctor(String userId, Doctor doctor);
	public List<DoctorResponse> seeAllDoctor();
	DoctorResponse findByUserId(String userId);
	List<DoctorResponse> findByYearOfExperiencesGreaterThan(int year);
	List<DoctorResponse> findByDesignationIgnoreCase(String designation);
	List<DoctorResponse> findByDegressContainingIgnoreCase(String degress);
	List<DoctorResponse> findByWorkingExperiencesContainingIgnoreCase(String workingExperiences);
	DoctorResponse findById(String doctorId);
	public Doctor updateDoctor(String userId, String doctorId, Doctor updatedDoctor);
	public boolean deleteDoctor(String userId, String doctorId);

}

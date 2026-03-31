package com.example.demo700.Repositories.FileUploadRepositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.FileUploadModel.ProfileIamge;

@Repository
public interface ProfileImageRepository extends MongoRepository<ProfileIamge, String> {

	public ProfileIamge findByUserId(String userId);
	public List<ProfileIamge> findByUserIdIn(List<String> userIds);

}

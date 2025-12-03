package com.example.demo700.Repositories.FileUploadRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.FileUploadModel.CVUploadModel;

@Repository
public interface CVUploadRepository extends MongoRepository<CVUploadModel, String> {

	public CVUploadModel findByUserId(String userId);

}

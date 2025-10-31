package com.example.demo700.Repositories.NotificationRepositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.NotificationModels.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
	
	List<Notification> findByUserIdAndReadFalse(String userId);

}

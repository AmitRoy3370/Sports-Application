package com.example.demo700.Repositories.ChatRepositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.ChatModels.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	
	List<ChatMessage> findBySenderAndReceiver(String sender, String receiver);
    List<ChatMessage> findByReceiverAndSender(String receiver, String sender);

}

package com.example.demo700.Services.ChatServices;

import java.util.List;

import com.example.demo700.DTOFiles.ChatMessageResponse;
import com.example.demo700.Models.ChatModels.ChatMessage;

public interface ChatService {
	
	public ChatMessage saveMessage(ChatMessage message);
	List<ChatMessageResponse> findByReceiverOrSender(String receiver, String sender);
	public List<ChatMessageResponse> getChatHistory(String user1, String user2);
	public ChatMessage getChatMessageById(String id);
	public List<ChatMessageResponse> seeAllChatMessage(String userId);
	public boolean deleteChatMessage(String sender, String receiver, String chatId);
	public ChatMessage editChatMessage(String sender, String chatId, String newContent);

}

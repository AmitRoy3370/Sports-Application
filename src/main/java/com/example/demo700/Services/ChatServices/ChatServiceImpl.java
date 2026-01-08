package com.example.demo700.Services.ChatServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.Models.User;
import com.example.demo700.Models.ChatModels.ChatMessage;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.ChatRepositories.ChatMessageRepository;
import com.example.demo700.Services.NotificationServices.NotificationService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private CyclicCleaner cleaner;

	@Autowired
	private UserRepository userRepository;

	@Override
	public ChatMessage saveMessage(ChatMessage message) {

		if (message == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User sender = userRepository.findById(message.getSender()).get();

			if (sender == null) {

				throw new Exception();

			}

			User receiver = userRepository.findById(message.getReceiver()).get();

			if (receiver == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("In valid sender or receiver user...");

		}

		message.setTimeStamp(Instant.now());
		ChatMessage saved = chatMessageRepository.save(message);
		
		User user = userRepository.findById(message.getSender()).get();
		
		// নোটিফিকেশন পাঠানো
		notificationService.sendNotification(message.getReceiver(), "New message from " + user.getName());
		return saved;
	}

	@Override
	public List<ChatMessage> getChatHistory(String user1, String user2) {
		List<ChatMessage> sent = chatMessageRepository.findBySenderAndReceiver(user1, user2);
		List<ChatMessage> received = chatMessageRepository.findByReceiverAndSender(user1, user2);
		sent.addAll(received);
		sent.sort((m1, m2) -> m1.getTimeStamp().compareTo(m2.getTimeStamp()));
		return sent;
	}

	@Override
	public boolean deleteChatMessage(String sender, String receiver, String chatId) {

		if (sender == null || receiver == null || chatId == null) {

			throw new NullPointerException("False request...");

		}

		long count = chatMessageRepository.count();

		try {

			List<ChatMessage> chatMessasge1 = chatMessageRepository.findBySenderAndReceiver(sender, receiver);

			if (chatMessasge1.isEmpty()) {

				throw new Exception();

			}

			List<ChatMessage> chatMessage2 = chatMessageRepository.findByReceiverAndSender(receiver, sender);

			if (chatMessage2.isEmpty()) {

				throw new Exception();

			}

			List<String> chatIds = new ArrayList<>();

			for (ChatMessage i : chatMessasge1) {

				chatIds.add(i.getId());

			}

			if (!chatIds.contains(chatId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("In valid sender or receiver data...");

		}

		cleaner.removeChatMessage(chatId);

		return count != chatMessageRepository.count();
	}

	@Override
	public ChatMessage editChatMessage(String sender, String chatId, String newContent) {

		if (sender == null || chatId == null || newContent == null) {
			throw new NullPointerException("False request...");
		}

		// Find the message
		ChatMessage message = chatMessageRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchElementException("Chat message not found..."));

		// Check sender ownership
		if (!message.getSender().equals(sender)) {
			throw new NoSuchElementException("You can edit only your own messages...");
		}

		// Update message
		message.setContent(newContent);
		message.setTimeStamp(Instant.now()); // update timestamp for last edit

		ChatMessage updated = chatMessageRepository.save(message);

		User user = userRepository.findById(message.getSender()).get();
		
		// Optional: Notify receiver about edit
		notificationService.sendNotification(message.getReceiver(), "Message edited by " + user.getName());

		return updated;
	}

}

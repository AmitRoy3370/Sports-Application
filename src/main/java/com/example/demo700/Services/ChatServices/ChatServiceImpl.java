package com.example.demo700.Services.ChatServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.ChatMessageResponse;
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
	public List<ChatMessageResponse> getChatHistory(String user1, String user2) {
		List<ChatMessage> sent = chatMessageRepository.findBySenderAndReceiver(user1, user2);
		List<ChatMessage> received = chatMessageRepository.findByReceiverAndSender(user1, user2);
		sent.addAll(received);
		sent.sort((m1, m2) -> m1.getTimeStamp().compareTo(m2.getTimeStamp()));
		return getChatMessageResponseFromChatList(sent);
	}

	@Override
	public boolean deleteChatMessage(String sender, String receiver, String chatId) {

		if (sender == null || receiver == null || chatId == null) {

			throw new NullPointerException("False request...");

		}

		long count = chatMessageRepository.count();

		try {

			ChatMessage chat = chatMessageRepository.findById(chatId).get();

			if (chat == null) {

				throw new Exception();

			}

			if (chat.getSender().equals(sender) && chat.getReceiver().equals(receiver)) {

			} else {

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

	private ChatMessageResponse getChatMessageResponseFromChat(ChatMessage chatMessage) {

		List<ChatMessage> chat = new ArrayList<>();
		chat.add(chatMessage);

		return getChatMessageResponseFromChatList(chat).get(0);

	}

	private List<ChatMessageResponse> getChatMessageResponseFromChatList(List<ChatMessage> chats) {

		List<ChatMessageResponse> responses = new ArrayList<>();

		List<String> receiversUserId = chats.stream().map(ChatMessage::getReceiver).distinct()
				.collect(Collectors.toList());
		List<String> sendersUserId = chats.stream().map(ChatMessage::getSender).distinct().collect(Collectors.toList());

		List<User> receiverUser = userRepository.findAllById(receiversUserId);
		List<User> senderUser = userRepository.findAllById(sendersUserId);

		Map<String, User> receiverMap = receiverUser.isEmpty() ? new HashMap<>()
				: receiverUser.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		Map<String, User> senderMap = senderUser.isEmpty() ? new HashMap<>()
				: senderUser.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		for (ChatMessage chat : chats) {

			ChatMessageResponse response = new ChatMessageResponse();

			response.setId(chat.getId());
			response.setReceiverId(chat.getReceiver());
			response.setSenderUserId(chat.getSender());
			response.setContent(chat.getContent());
			response.setTimeStamp(chat.getTimeStamp());

			try {

				response.setReceiverName(receiverMap.get(chat.getReceiver()).getName());
				response.setSenderUserName(senderMap.get(chat.getSender()).getName());

			} catch (Exception e) {

			}

			responses.add(response);

		}

		return responses;

	}

	@Override
	public ChatMessageResponse getChatMessageById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			ChatMessage chat = chatMessageRepository.findById(id).get();

			if (chat == null) {

				throw new Exception();

			}

			return getChatMessageResponseFromChat(chat);

		} catch (Exception e) {

			throw new NoSuchElementException("No such chat find at here....");

		}

	}

	@Override
	public List<ChatMessageResponse> seeAllChatMessage() {

		try {

			List<ChatMessage> list = chatMessageRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getChatMessageResponseFromChatList(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such chat find at here...");

		}

	}

}

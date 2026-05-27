package com.example.demo700.Services.ChatServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.ChatMessageResponse;

import com.example.demo700.Models.User;
import com.example.demo700.Models.ChatModels.ChatMessage;
import com.example.demo700.Models.ChatModels.ReadableChat;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.ChatRepositories.ChatMessageRepository;
import com.example.demo700.Repositories.ChatRepositories.ReadableChatRepository;
import com.example.demo700.Services.NotificationServices.NotificationService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ReadableChatRepository readChatRepository;

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

		if (saved != null) {

			readChatRepository.save(new ReadableChat(saved.getId(), false));

		}

		User user = userRepository.findById(message.getSender()).get();

		// নোটিফিকেশন পাঠানো
		notificationService.sendNotification(message.getReceiver(), "New message from " + user.getName());
		return saved;
	}

	@Override
	public List<ChatMessageResponse> findByReceiverOrSender(String receiver, String sender) {

		if (sender == null || receiver == null) {

			throw new NullPointerException("False request...");

		}

		List<User> users = userRepository.findAllById(List.of(sender, receiver));

		List<ChatMessageResponse> list = getChatResponseFromListUser(sender, users);

		if (list == null || list.isEmpty()) {

			if (!sender.equals(receiver)) {

				List<ChatMessageResponse> _list = getChatResponseFromListUser(receiver, users);

				if (!_list.isEmpty()) {

					return _list;

				}

			}

			throw new NoSuchElementException("No chat find...");

		}

		if (!sender.equals(receiver)) {

			List<ChatMessageResponse> _list = getChatResponseFromListUser(receiver, users);

			if (!_list.isEmpty()) {

				list.addAll(_list);

			}

		}

		return list;

	}

	@Override
	public List<ChatMessage> getChatHistory(String user1, String user2) {

		if(user1 == null || user2 == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		List<ChatMessage> list = chatMessageRepository.findByReceiverAndSender(user1, user2);
		
		if(list.isEmpty()) {
			
			throw new NoSuchElementException("No such chat yet happened");
			
		}
		
		return list;
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

		try {

			if (updated != null) {

				ReadableChat readChat = null;

				try {

					readChatRepository.findByChatId(updated.getId());

				} catch (Exception e) {

				}

				if (readChat != null) {

					readChat.setRead(false);

					readChatRepository.save(readChat);

				} else {

					readChatRepository.save(new ReadableChat(updated.getId(), false));

				}

			}

		} catch (Exception e) {

		}

		User user = userRepository.findById(message.getSender()).get();

		// Optional: Notify receiver about edit
		notificationService.sendNotification(message.getReceiver(), "Message edited by " + user.getName());

		return updated;
	}

	@Override
	public ChatMessage getChatMessageById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			ChatMessage chat = chatMessageRepository.findById(id).get();

			if (chat == null) {

				throw new Exception();

			}

			return (chat);

		} catch (Exception e) {

			throw new NoSuchElementException("No such chat find at here....");

		}

	}

	@Override
	public List<ChatMessageResponse> seeAllChatMessage(String userId) {

		try {

			List<User> list = userRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getChatResponseFromListUser(userId, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such chat find at here...");

		}

	}

	private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private List<ChatMessageResponse> getChatResponseFromListUser(String userId, List<User> allUsers) {

		List<ChatMessageResponse> responses = new ArrayList<>();

		CompletableFuture<Map<String, Boolean>> readChatFuture = CompletableFuture.supplyAsync(
				() -> readChatRepository.findByRead(false).stream().collect(Collectors.toMap(ReadableChat::getChatId,
						readChat -> readChat.isRead(), (existing, replacement) -> existing)),
				executors);

		Map<String, Boolean> readChat = readChatFuture.join();

		CompletableFuture<Map<String, User>> nameFutures = CompletableFuture
				.supplyAsync(
						() -> allUsers
								.isEmpty()
										? new HashMap<>()
										: allUsers.stream().filter(Objects::nonNull).collect(Collectors.toMap(
												User::getId, Function.identity(), (existing, replacement) -> existing)),
						executors);

		CompletableFuture<List<ChatMessage>> senderMessagesFuture = CompletableFuture
				.supplyAsync(() -> chatMessageRepository.findBySender(userId), executors);

		CompletableFuture<List<ChatMessage>> receiverMessagesFuture = CompletableFuture
				.supplyAsync(() -> chatMessageRepository.findByReceiver(userId), executors);

		CompletableFuture<Map<String, ChatMessage>> latestMessageFuture = CompletableFuture.supplyAsync(() -> {
			List<ChatMessage> messages = chatMessageRepository.findAllConversationsWithLatestMessage(userId);
			if (messages == null || messages.isEmpty()) {
				return new HashMap<>();
			}

			return messages.stream().filter(Objects::nonNull).filter(msg -> {
				// Check for null sender or receiver
				if (msg.getSender() == null || msg.getReceiver() == null) {
					System.err.println("Skipping message with null sender/receiver: " + msg.getId());
					return false;
				}
				return true;
			}).collect(Collectors.toMap(msg -> {
				// Safe equals check with null handling
				String sender = msg.getSender();
				String receiver = msg.getReceiver();
				String otherUserId;

				if (userId != null && userId.equals(sender)) {
					otherUserId = receiver;
				} else {
					otherUserId = sender;
				}

				return otherUserId != null ? otherUserId : "unknown_" + System.currentTimeMillis();
			}, Function.identity(), (existing, replacement) -> {
				// Keep the existing message (or you can compare timestamps)
				return existing;
			}));
		}, executors);

		CompletableFuture.allOf(nameFutures, senderMessagesFuture, receiverMessagesFuture, latestMessageFuture).join();

		Map<String, User> userMap = nameFutures.join();
		Map<String, ChatMessage> latestMessageMap = latestMessageFuture.join();
		List<ChatMessage> senderMessages = senderMessagesFuture.join();
		List<ChatMessage> receiverMessages = receiverMessagesFuture.join();

		// Build a composite key map for O(1) lookups: "sender_receiver" or
		// "receiver_sender"
		Map<String, ChatMessage> messageLookupMap = buildMessageLookupMap(senderMessages, receiverMessages);

		List<String> allUserId = allUsers.stream().filter(Objects::nonNull).map(User::getId)
				.collect(Collectors.toList());

		for (String otherUserId : allUserId) {

			try {

				User otherUser = userMap.get(otherUserId);
				if (otherUser == null)
					continue;

				ChatMessageResponse response = new ChatMessageResponse();

				User currentUser = userMap.get(userId);

				try {

					response.setSenderId(userId);
					response.setSenderName(currentUser != null ? currentUser.getName() : "Unknown");

				} catch (Exception e) {

				}

				// O(1) lookup instead of O(n) iteration
				ChatMessage latestMessage = getLatestMessageFromMap(userId, otherUserId, messageLookupMap,
						latestMessageMap);

				if (latestMessage != null) {
					response.setId(latestMessage.getId());
					response.setTimeStamp(latestMessage.getTimeStamp());

					try {

						boolean isCurrentUserSender = latestMessage.getSender().equals(userId);

						if (isCurrentUserSender) {
							response.setSenderInfo(new ChatMessageResponse.SenderInfo(otherUser.getName(), otherUserId,
									latestMessage.getContent(), readChat.getOrDefault(latestMessage.getId(), true)));
							response.setReceiverInfo(null);
						} else {
							response.setSenderInfo(null);
							response.setReceiverInfo(new ChatMessageResponse.ReceiverInfo(otherUserId,
									otherUser.getName(), latestMessage.getContent(),
									readChat.getOrDefault(latestMessage.getId(), true)));
						}

					} catch (Exception e) {

					}

				} else {

					try {

						response.setId(null);
						response.setTimeStamp(null);
						response.setSenderInfo(
								new ChatMessageResponse.SenderInfo(otherUser.getName(), otherUserId, "", true));
						response.setReceiverInfo(null);

					} catch (Exception e) {

					}

				}

				responses.add(response);

			} catch (Exception e) {

			}

		}

		try {

			responses.sort((r1, r2) -> {
				if (r1.getTimeStamp() == null && r2.getTimeStamp() == null)
					return 0;
				if (r1.getTimeStamp() == null)
					return 1;
				if (r2.getTimeStamp() == null)
					return -1;
				return r2.getTimeStamp().compareTo(r1.getTimeStamp());
			});

		} catch (Exception e) {

		}

		return responses;

	}

	private List<ChatMessageResponse> getChatResponseFromUser(String userId) {
		List<ChatMessageResponse> responses = new ArrayList<>();

		List<User> allUsers = userRepository.findAll();

		CompletableFuture<Map<String, Boolean>> readChatFuture = CompletableFuture.supplyAsync(
				() -> readChatRepository.findByRead(false).stream().collect(Collectors.toMap(ReadableChat::getChatId,
						readChat -> readChat.isRead(), (existing, replacement) -> existing)),
				executors);

		Map<String, Boolean> readChat = readChatFuture.join();

		CompletableFuture<Map<String, User>> nameFutures = CompletableFuture
				.supplyAsync(
						() -> allUsers
								.isEmpty()
										? new HashMap<>()
										: allUsers.stream().filter(Objects::nonNull).collect(Collectors.toMap(
												User::getId, Function.identity(), (existing, replacement) -> existing)),
						executors);

		CompletableFuture<List<ChatMessage>> senderMessagesFuture = CompletableFuture
				.supplyAsync(() -> chatMessageRepository.findBySender(userId), executors);

		CompletableFuture<List<ChatMessage>> receiverMessagesFuture = CompletableFuture
				.supplyAsync(() -> chatMessageRepository.findByReceiver(userId), executors);

		CompletableFuture<Map<String, ChatMessage>> latestMessageFuture = CompletableFuture.supplyAsync(() -> {
			List<ChatMessage> messages = chatMessageRepository.findAllConversationsWithLatestMessage(userId);
			if (messages == null || messages.isEmpty()) {
				return new HashMap<>();
			}

			return messages.stream().filter(Objects::nonNull).filter(msg -> {
				// Check for null sender or receiver
				if (msg.getSender() == null || msg.getReceiver() == null) {
					System.err.println("Skipping message with null sender/receiver: " + msg.getId());
					return false;
				}
				return true;
			}).collect(Collectors.toMap(msg -> {
				// Safe equals check with null handling
				String sender = msg.getSender();
				String receiver = msg.getReceiver();
				String otherUserId;

				if (userId != null && userId.equals(sender)) {
					otherUserId = receiver;
				} else {
					otherUserId = sender;
				}

				return otherUserId != null ? otherUserId : "unknown_" + System.currentTimeMillis();
			}, Function.identity(), (existing, replacement) -> {
				// Keep the existing message (or you can compare timestamps)
				return existing;
			}));
		}, executors);

		CompletableFuture.allOf(nameFutures, senderMessagesFuture, receiverMessagesFuture, latestMessageFuture).join();

		Map<String, User> userMap = nameFutures.join();
		Map<String, ChatMessage> latestMessageMap = latestMessageFuture.join();
		List<ChatMessage> senderMessages = senderMessagesFuture.join();
		List<ChatMessage> receiverMessages = receiverMessagesFuture.join();

		// Build a composite key map for O(1) lookups: "sender_receiver" or
		// "receiver_sender"
		Map<String, ChatMessage> messageLookupMap = buildMessageLookupMap(senderMessages, receiverMessages);

		List<String> allUserId = allUsers.stream().map(User::getId).collect(Collectors.toList());

		for (String otherUserId : allUserId) {

			try {

				User otherUser = userMap.get(otherUserId);
				if (otherUser == null)
					continue;

				ChatMessageResponse response = new ChatMessageResponse();

				User currentUser = userMap.get(userId);

				try {

					response.setSenderId(userId);
					response.setSenderName(currentUser != null ? currentUser.getName() : "Unknown");

				} catch (Exception e) {

				}

				// O(1) lookup instead of O(n) iteration
				ChatMessage latestMessage = getLatestMessageFromMap(userId, otherUserId, messageLookupMap,
						latestMessageMap);

				if (latestMessage != null) {

					try {

						response.setId(latestMessage.getId());
						response.setTimeStamp(latestMessage.getTimeStamp());

					} catch (Exception e) {

					}

					try {

						boolean isCurrentUserSender = latestMessage.getSender().equals(userId);

						if (isCurrentUserSender) {
							response.setSenderInfo(new ChatMessageResponse.SenderInfo(otherUser.getName(), otherUserId,
									latestMessage.getContent(), readChat.getOrDefault(latestMessage.getId(),
											readChat.getOrDefault(latestMessage.getId(), true))));
							response.setReceiverInfo(null);
						} else {
							response.setSenderInfo(null);
							response.setReceiverInfo(
									new ChatMessageResponse.ReceiverInfo(otherUserId, otherUser.getName(),
											latestMessage.getContent(), readChat.getOrDefault(latestMessage.getId(),
													readChat.getOrDefault(latestMessage.getId(), true))));
						}

					} catch (Exception e) {

					}

				} else {

					try {

						response.setId(null);
						response.setTimeStamp(null);
						response.setSenderInfo(
								new ChatMessageResponse.SenderInfo(otherUser.getName(), otherUserId, "", true));
						response.setReceiverInfo(null);

					} catch (Exception e) {

					}

				}

				responses.add(response);

			} catch (Exception e) {

			}

		}

		try {

			responses.sort((r1, r2) -> {
				if (r1.getTimeStamp() == null && r2.getTimeStamp() == null)
					return 0;
				if (r1.getTimeStamp() == null)
					return 1;
				if (r2.getTimeStamp() == null)
					return -1;
				return r2.getTimeStamp().compareTo(r1.getTimeStamp());
			});

		} catch (Exception e) {

		}

		return responses;
	}

	// Build a map with composite key for O(1) lookups
	private Map<String, ChatMessage> buildMessageLookupMap(List<ChatMessage> senderMessages,
			List<ChatMessage> receiverMessages) {
		Map<String, ChatMessage> lookupMap = new HashMap<>();

		// Add sender messages with composite key "sender_receiver"
		for (ChatMessage msg : senderMessages) {
			String key = msg.getSender() + "_" + msg.getReceiver();
			ChatMessage existing = lookupMap.get(key);
			if (existing == null || msg.getTimeStamp().isAfter(existing.getTimeStamp())) {
				lookupMap.put(key, msg);
			}
		}

		// Add receiver messages with composite key "sender_receiver" (swap to maintain
		// consistency)
		for (ChatMessage msg : receiverMessages) {
			String key = msg.getSender() + "_" + msg.getReceiver();
			ChatMessage existing = lookupMap.get(key);
			if (existing == null || msg.getTimeStamp().isAfter(existing.getTimeStamp())) {
				lookupMap.put(key, msg);
			}
		}

		return lookupMap;
	}

	// O(1) lookup using composite key
	private ChatMessage getLatestMessageFromMap(String userId, String otherUserId,
			Map<String, ChatMessage> messageLookupMap, Map<String, ChatMessage> latestMessageMap) {
		// First try to get from pre-computed latestMessageMap (most efficient)
		ChatMessage latestMessage = latestMessageMap.get(otherUserId);
		if (latestMessage != null) {
			return latestMessage;
		}

		// Fallback to composite key lookup
		String key1 = userId + "_" + otherUserId;
		String key2 = otherUserId + "_" + userId;

		ChatMessage msg1 = messageLookupMap.get(key1);
		ChatMessage msg2 = messageLookupMap.get(key2);

		if (msg1 == null)
			return msg2;
		if (msg2 == null)
			return msg1;

		// Return the most recent one
		return msg1.getTimeStamp().isAfter(msg2.getTimeStamp()) ? msg1 : msg2;
	}

}

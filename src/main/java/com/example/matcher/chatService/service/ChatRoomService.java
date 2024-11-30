package com.example.matcher.chatService.service;

import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.dto.ChatRoomWithLastMessageDTO;
import com.example.matcher.chatService.exception.InvalidCredentialsException;
import com.example.matcher.chatService.exception.ResourceAlreadyExistsException;
import com.example.matcher.chatService.exception.ResourceNotFoundException;
import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);


    private ChatRoomRepository chatRoomRepository;
    public Message addNewMessage(Message message, Long chatId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElseThrow(()
                -> new ResourceNotFoundException("Chat Id: " + chatId + " not found"));
        message.setChatRoom(chatRoom);
        chatRoom.getMessageList().add(message);
        chatRoom.setTimeLastUpdate(LocalDateTime.now());
        chatRoom = chatRoomRepository.save(chatRoom);
        message = chatRoom.getMessageList().get(chatRoom.getMessageList().size() - 1);
        return message;
    }



    public ChatRoom createNewChat(UUID firstUserId, UUID secondUserId) {
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findChatRoomBetweenUsers(firstUserId, secondUserId);
        if (existingChatRoom.isPresent()) {
            throw new ResourceAlreadyExistsException("Chat already exists.");
        }
        //  Проверить взаимные лайки можно ли создавать чат
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setFirstUserId(firstUserId);
        chatRoom.setSecondUserId(secondUserId);
        chatRoom.setMessageList(new ArrayList<>());
        LocalDateTime currentTime = LocalDateTime.now();
        chatRoom.setTimeCreated(currentTime);
        chatRoom.setTimeLastUpdate(currentTime);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public List<Message> getHistoryChat(UUID senderId, UUID recipientId) {
        return chatRoomRepository.findChatHistoryBetweenUsers(senderId, recipientId);
    }

    public List<Message> getHistoryChat(Long chatId) {
        return chatRoomRepository.findChatHistoryByChatId(chatId);
    }

    public List<ChatRoomWithLastMessageDTO> getListLastChatRooms(UUID userId) {
        List<Object[]> results = chatRoomRepository.findListLastChatRoomsWithLastMessage(userId);
        List<ChatRoomWithLastMessageDTO> chatRoomWithLastMessageDTOList = results.stream()
                .map(result -> {
                    ChatRoom chatRoom = (ChatRoom) result[0];
                    Message lastMessage = (Message) result[1];
                    return new ChatRoomWithLastMessageDTO(
                            chatRoom.getId(),
                            chatRoom.getFirstUserId(),
                            chatRoom.getSecondUserId(),
                            chatRoom.getTimeLastUpdate(),
                            chatRoom.getTimeCreated(),
                            lastMessage
                    );
                })
                .collect(Collectors.toList());
        logger.info("Chat room success: " + chatRoomWithLastMessageDTOList.size());
        return chatRoomWithLastMessageDTOList;
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public UUID getIdAnotherUser(Long chatRoomId, UUID firstUserId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()
                -> new ResourceNotFoundException("Chat doesnt exist"));
        UUID targetUserId;
        if (chatRoom.getFirstUserId().equals(firstUserId)) {
            targetUserId = chatRoom.getSecondUserId();
        } else if (chatRoom.getSecondUserId().equals(firstUserId)) {
            targetUserId = chatRoom.getFirstUserId();
        } else {
            throw new InvalidCredentialsException("Illegal access");
        }
        return targetUserId;
    }

//    public String deleteMessage(Message message, Long chatRoomId) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()
//                -> new ResourceNotFoundException("Chat with id: " + chatRoomId + "doesnt exist"));
//        message.setChatRoom(chatRoom);
//        chatRoom.getMessageList().remove(message);
//        chatRoomRepository.save(chatRoom); // Сообщение будет удалено из БД
//        return "Success";
//    }
}


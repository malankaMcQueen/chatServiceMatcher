package com.example.matcher.chatService.service;

import com.example.matcher.chatService.dto.ChatRoomDTO;
import com.example.matcher.chatService.exception.ResourceNotFoundException;
import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
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

    private ChatRoomRepository chatRoomRepository;
    public void addNewMessage(Message message, Long chatId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElseThrow(()
                -> new ResourceNotFoundException("Chat Id: " + chatId + " not found"));
        message.setChatRoom(chatRoom);
        chatRoom.getMessageList().add(message);
        chatRoom.setTimeLastUpdate(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
    }



    public ChatRoom createNewChat(UUID firstUserId, UUID secondUserId) {
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findChatRoomBetweenUsers(firstUserId, secondUserId);
        if (existingChatRoom.isPresent()) {
            throw new IllegalStateException("Chat already exists.");
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

    public List<ChatRoomDTO> getListLastChatRooms(UUID userId) {
        List<Object[]> results = chatRoomRepository.findListLastChatRoomsWithLastMessage(userId);
        List<ChatRoomDTO> chatRoomDTOList = results.stream()
                .map(result -> {
                    ChatRoom chatRoom = (ChatRoom) result[0];
                    Message lastMessage = (Message) result[1];
                    return new ChatRoomDTO(
                            chatRoom.getId(),
                            chatRoom.getFirstUserId(),
                            chatRoom.getSecondUserId(),
                            chatRoom.getTimeLastUpdate(),
                            chatRoom.getTimeCreated(),
                            lastMessage
                    );
                })
                .collect(Collectors.toList());
        return chatRoomDTOList;
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public String deleteMessage(Message message, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()
                -> new ResourceNotFoundException("Chat with id: " + chatRoomId + "doesnt exist"));
        message.setChatRoom(chatRoom);
        chatRoom.getMessageList().remove(message);
        chatRoomRepository.save(chatRoom); // Сообщение будет удалено из БД
        return "Success";
    }
}


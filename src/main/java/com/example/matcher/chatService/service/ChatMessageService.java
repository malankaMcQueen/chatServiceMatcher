package com.example.matcher.chatService.service;

import com.example.matcher.chatService.aspect.AspectAnnotation;
import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.dto.NewMessageDTO;
import com.example.matcher.chatService.exception.BadRequestException;
import com.example.matcher.chatService.exception.ResourceNotFoundException;
import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.model.MessageStatus;
import com.example.matcher.chatService.repository.ChatMessageRepository;
import com.example.matcher.chatService.repository.ChatRoomRepository;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatMessageService {

    private ChatMessageRepository chatMessageRepository;
    private ChatRoomService chatRoomService;
    private SimpMessagingTemplate messagingTemplate;
    private ChatRoomRepository chatRoomRepository;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);


    public void saveNewMessage(NewMessageDTO newMessageDTO) {
        Message message = new Message();
        message.setContent(newMessageDTO.getContent());
        message.setSenderId(newMessageDTO.getSenderId());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.RECEIVED);
        chatRoomService.addNewMessage(message, newMessageDTO.getChatRoomId()); // Сохранит Message через каскад
    }

    @Transactional
    public void sendNewMessage(NewMessageDTO newMessageDTO) {
        ChatRoom chatRoom = chatRoomRepository.findById(newMessageDTO.getChatRoomId()).orElseThrow(()
                -> new ResourceNotFoundException("Chat with id: " + newMessageDTO.getChatRoomId() + "not exist"));
        UUID targetUserId;
        if (chatRoom.getFirstUserId().equals(newMessageDTO.getSenderId())) {
            targetUserId = chatRoom.getSecondUserId();
        } else if (chatRoom.getSecondUserId().equals(newMessageDTO.getSenderId())) {
            targetUserId = chatRoom.getFirstUserId();
        } else {
            throw new BadRequestException("Some Error");
        }
        saveNewMessage(newMessageDTO);
        messagingTemplate.convertAndSendToUser(
                targetUserId.toString(), "/queue/messages", newMessageDTO);
    }


    public String deleteMessage(Message message) {
        chatMessageRepository.deleteById(message.getId());
        return "Success";
    }

    public Message editMessage(Message newMessage) {
        Message message = chatMessageRepository.findById(newMessage.getId()).orElseThrow(()
                -> new ResourceNotFoundException("message with id: " + newMessage.getId() + "doesnt exist"));
        if (!message.getContent().equals(newMessage.getContent())) {
            message.setContent(newMessage.getContent());
            chatMessageRepository.save(message);
        }
        return message;
    }
}
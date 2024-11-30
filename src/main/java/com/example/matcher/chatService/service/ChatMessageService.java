package com.example.matcher.chatService.service;

import com.example.matcher.chatService.aspect.AspectAnnotation;
import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.dto.message.DeleteMessageDTO;
import com.example.matcher.chatService.dto.message.EditMessageDTO;
import com.example.matcher.chatService.dto.message.NewMessageDTO;
import com.example.matcher.chatService.dto.message.ReadMessageDTO;
import com.example.matcher.chatService.exception.BadRequestException;
import com.example.matcher.chatService.exception.InvalidCredentialsException;
import com.example.matcher.chatService.exception.ResourceNotFoundException;
import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.model.MessageStatus;
import com.example.matcher.chatService.repository.ChatMessageRepository;
import com.example.matcher.chatService.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatMessageService {

    private ChatMessageRepository chatMessageRepository;
    private ChatRoomService chatRoomService;
    private SimpMessagingTemplate messagingTemplate;
    private ChatRoomRepository chatRoomRepository;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);


    public Message saveNewMessage(NewMessageDTO newMessageDTO) {
        Message message = new Message();
        message.setContent(newMessageDTO.getContent());
        message.setSenderId(newMessageDTO.getSenderId());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.DELIVERY);
        message = chatRoomService.addNewMessage(message, newMessageDTO.getChatRoomId()); // Сохранит Message через каскад
        return message;
    }

    public void markMessageAsRead(ReadMessageDTO readMessage) {     // TODO Сделать запрос в БД на update вместо вытягивания, изменения и сохранения
        Message message = chatMessageRepository.findById(readMessage.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        message.setStatus(MessageStatus.READ);
        chatMessageRepository.save(message);
        String topic = "/ChatService/topic/user/" + message.getSenderId() + "/message/read";
        messagingTemplate.convertAndSend(topic, message);
    }


    @Transactional
    public void sendNewMessage(NewMessageDTO newMessageDTO) {
        if (newMessageDTO.getContent().isEmpty()) {
            return;
        }
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
        Message message = saveNewMessage(newMessageDTO);
        String topic = "/ChatService/topic/user/" + message.getSenderId() + "/message/confirmations";
        messagingTemplate.convertAndSend(topic, message);
        messagingTemplate.convertAndSendToUser(
                targetUserId.toString(), "/queue/messages", message);

    }


    public String deleteMessage(DeleteMessageDTO messageDTO) {
        UUID targetUserId = chatRoomService.getIdAnotherUser(messageDTO.getChatRoomId(), messageDTO.getSenderId());
        chatMessageRepository.deleteById(messageDTO.getMessageId());
        String topic = "/ChatService/topic/user/" + targetUserId.toString() + "/message/delete";
        messagingTemplate.convertAndSend(topic, messageDTO);
        return "Success";
    }

    public Message editMessage(EditMessageDTO newMessage) {
        Message message = chatMessageRepository.findById(newMessage.getMessageId()).orElseThrow(()
                -> new ResourceNotFoundException("message with id: " + newMessage.getMessageId() + "doesnt exist"));
        if (newMessage.getNewContent().isEmpty()) {
            DeleteMessageDTO deleteMessageDTO = new DeleteMessageDTO();
            deleteMessageDTO.setMessageId(newMessage.getMessageId());
            deleteMessageDTO.setChatRoomId(newMessage.getChatRoomId());
            deleteMessageDTO.setSenderId(newMessage.getSenderId());
            this.deleteMessage(deleteMessageDTO);
            message.setContent("");
        }
        else {
            UUID targetUserId = chatRoomService.getIdAnotherUser(newMessage.getChatRoomId(), newMessage.getSenderId());
            if (message.getSenderId().equals(newMessage.getSenderId())) {
                if (!message.getContent().equals(newMessage.getNewContent())) {
                    message.setContent(newMessage.getNewContent());
                    chatMessageRepository.save(message);
                    String topic = "/ChatService/topic/user/" + targetUserId.toString() + "/message/edit";
                    messagingTemplate.convertAndSend(topic, newMessage);
                }
            }
        }
        return message;
    }
}




package com.example.matcher.chatService.service;

import com.example.matcher.chatService.dto.message.NewMessageDTO;
import com.example.matcher.chatService.exception.BadRequestException;
import com.example.matcher.chatService.exception.ResourceNotFoundException;
import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MediaService {
    ChatMessageService messageService;
    public Message uploadPhoto(Long chatId, UUID senderId, MultipartFile file) {

        String photoURL = "asd";
        NewMessageDTO newMessageDTO = NewMessageDTO.builder()
                .chatRoomId(chatId)
                .senderId(senderId)
                .content(photoURL)
                .build();
        messageService.sendNewMessage(newMessageDTO);

        return null;
    }

//    public void sendNewMessage(NewMessageDTO newMessageDTO) {
//        if (newMessageDTO.getContent().isEmpty()) {
//            return;
//        }
//        ChatRoom chatRoom = chatRoomRepository.findById(newMessageDTO.getChatRoomId()).orElseThrow(()
//                -> new ResourceNotFoundException("Chat with id: " + newMessageDTO.getChatRoomId() + "not exist"));
//        UUID targetUserId;
//        if (chatRoom.getFirstUserId().equals(newMessageDTO.getSenderId())) {
//            targetUserId = chatRoom.getSecondUserId();
//        } else if (chatRoom.getSecondUserId().equals(newMessageDTO.getSenderId())) {
//            targetUserId = chatRoom.getFirstUserId();
//        } else {
//            throw new BadRequestException("Some Error");
//        }
//        Message message = saveNewMessage(newMessageDTO);
//        String topic = "/ChatService/topic/user/" + message.getSenderId() + "/message/confirmations";
//        messagingTemplate.convertAndSend(topic, message);
//        messagingTemplate.convertAndSendToUser(
//                targetUserId.toString(), "/queue/messages", message);
//
//    }
}

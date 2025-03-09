package com.example.matcher.chatService.service;

import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.dto.message.NewMessageDTO;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.model.MessageType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MediaService {
    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);
    private final S3Service s3Service;

    ChatMessageService messageService;
    public Message uploadPhoto(Long chatId, UUID senderId, MultipartFile file) throws IOException {
        File processedFile = ImageProcessor.processImageWithThumbnailator(file);
        String photoUrl = s3Service.uploadFile(chatId, processedFile);
        if (!processedFile.delete()) {
            logger.warn("Failed to delete temporary processed file: " + processedFile.getAbsolutePath());
        }
        NewMessageDTO newMessageDTO = NewMessageDTO.builder()
                .chatRoomId(chatId)
                .senderId(senderId)
                .content(photoUrl)
                .messageType(MessageType.PHOTO)
                .build();
        Message message = null;
        try {
            message = messageService.sendNewMessage(newMessageDTO);
        } catch (Exception e) {
            s3Service.deleteFile(photoUrl);
            throw new RuntimeException("Message saving failed, rollback photo upload", e);
        }
        return message;
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

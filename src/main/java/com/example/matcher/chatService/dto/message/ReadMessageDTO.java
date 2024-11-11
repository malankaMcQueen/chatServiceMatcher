package com.example.matcher.chatService.dto.message;

import com.example.matcher.chatService.model.MessageStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class ReadMessageDTO {
    private Long id;
    private Long chatRoomId;
    private UUID senderId;
    private String content;
    private MessageStatus status;
}

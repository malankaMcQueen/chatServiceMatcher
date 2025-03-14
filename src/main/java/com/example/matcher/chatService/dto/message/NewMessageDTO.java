package com.example.matcher.chatService.dto.message;

import com.example.matcher.chatService.model.MessageStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class NewMessageDTO {
    private UUID senderId;
    private Long chatRoomId;
    private String content;
}

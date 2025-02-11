package com.example.matcher.chatService.dto.message;

import lombok.Data;

import java.util.UUID;

@Data
public class NewMessageDTO {
    private UUID senderId;
    private Long chatRoomId;
    private String content;
    private Long replyToMessageId;
}

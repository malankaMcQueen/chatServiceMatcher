package com.example.matcher.chatService.dto.message;

import lombok.Data;

import java.util.UUID;

@Data
public class EditMessageDTO {
    private Long messageId;
    private Long chatRoomId;
    private UUID senderId;
    private String newContent;
}

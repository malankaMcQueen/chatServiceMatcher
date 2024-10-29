package com.example.matcher.chatService.dto.message;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteMessageDTO {
    private Long messageId;
    private UUID senderId;
    private Long chatRoomId;
}

package com.example.matcher.chatService.dto.message;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewMessageDTO {
    private UUID senderId;
    private Long chatRoomId;
    private String content;
    private Long replyToMessageId;
}

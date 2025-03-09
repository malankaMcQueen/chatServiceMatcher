package com.example.matcher.chatService.dto.message;

import com.example.matcher.chatService.model.MessageType;
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
    private MessageType messageType;
}

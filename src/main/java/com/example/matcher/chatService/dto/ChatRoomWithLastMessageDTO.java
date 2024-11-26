package com.example.matcher.chatService.dto;

import com.example.matcher.chatService.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ChatRoomWithLastMessageDTO {
    private Long id;
    private UUID firstUserId;
    private UUID secondUserId;
    private LocalDateTime timeLastUpdate;
    private LocalDateTime timeCreated;
    Message lastMessage;
}

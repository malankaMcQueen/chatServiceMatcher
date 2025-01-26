package com.example.matcher.chatService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TypingMessageDTO {
    UUID senderId;
    Long chatRoomId;
    StatusTyping statusTyping;
}

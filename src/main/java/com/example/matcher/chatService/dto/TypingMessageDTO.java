package com.example.matcher.chatService.dto;

import com.example.matcher.chatService.dto.StatusTyping;
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

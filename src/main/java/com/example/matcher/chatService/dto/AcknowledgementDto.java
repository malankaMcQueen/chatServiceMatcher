package com.example.matcher.chatService.dto;

import com.example.matcher.chatService.model.MessageStatus;
import lombok.Data;

@Data
public class AcknowledgementDto {
    private Long messageId;
    private MessageStatus status;
}

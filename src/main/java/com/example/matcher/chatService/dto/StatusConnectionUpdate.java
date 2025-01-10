package com.example.matcher.chatService.dto;

import com.example.matcher.chatService.model.StatusConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class StatusConnectionUpdate {
    UUID userId;
    StatusConnection statusConnection;
}

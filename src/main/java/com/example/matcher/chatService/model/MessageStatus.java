package com.example.matcher.chatService.model;

import lombok.Getter;

@Getter
public enum MessageStatus {
    DELIVERY(0),
    READ(1);

    private final int code;

    MessageStatus(int code) {
        this.code = code;
    }

    public static MessageStatus fromCode(int code) {
        for (MessageStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null; // Или выбрасывать исключение, если нужно
    }
}

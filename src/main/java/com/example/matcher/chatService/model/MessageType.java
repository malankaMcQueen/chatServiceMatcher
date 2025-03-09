package com.example.matcher.chatService.model;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum MessageType {
    TEXT(1),
    PHOTO(2);

    private static final Map<Integer, MessageType> MAP = Stream.of(values())
            .collect(Collectors.toMap(MessageType::getCode, e -> e));

    private final int code;

    MessageType(int code) {
        this.code = code;
    }

    public static MessageType fromCode(Integer code) {
        return MAP.getOrDefault(code, null);
    }
}

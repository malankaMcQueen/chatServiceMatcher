package com.example.matcher.chatService.model.converter;

import com.example.matcher.chatService.model.MessageStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class MessageStatusConverter implements AttributeConverter<MessageStatus, Integer> {

    private static final Map<Integer, MessageStatus> STATUS_MAP = Stream.of(MessageStatus.values())
            .collect(Collectors.toMap(MessageStatus::getCode, status -> status));

    @Override
    public Integer convertToDatabaseColumn(MessageStatus attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public MessageStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? STATUS_MAP.getOrDefault(dbData, null) : null;
    }
}

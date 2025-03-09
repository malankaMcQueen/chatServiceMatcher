package com.example.matcher.chatService.model.converter;

import com.example.matcher.chatService.model.MessageType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MessageTypeConverter implements AttributeConverter<MessageType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MessageType attribute) {
        return (attribute != null) ? attribute.getCode() : null;
    }

    @Override
    public MessageType convertToEntityAttribute(Integer dbData) {
        return (dbData != null) ? MessageType.fromCode(dbData) : null;
    }
}

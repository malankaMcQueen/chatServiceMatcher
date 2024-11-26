package com.example.matcher.chatService.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(final String message) {
        super(message);
    }
}

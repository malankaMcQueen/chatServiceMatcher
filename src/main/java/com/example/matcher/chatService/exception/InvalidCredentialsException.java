package com.example.matcher.chatService.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(final String msg) {
        super(msg);
    }
}

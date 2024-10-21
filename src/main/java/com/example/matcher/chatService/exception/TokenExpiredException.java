package com.example.matcher.chatService.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(final String msg) {
        super(msg);
    }
}


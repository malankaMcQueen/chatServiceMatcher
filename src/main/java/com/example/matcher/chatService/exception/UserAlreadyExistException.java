package com.example.matcher.chatService.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(final String msg) {
        super(msg);
    }

}

package com.example.adminservice.Config.Exceptions;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String message) {
        super(message);
    }
}
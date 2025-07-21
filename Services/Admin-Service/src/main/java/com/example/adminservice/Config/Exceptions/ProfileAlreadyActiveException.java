package com.example.adminservice.Config.Exceptions;

public class ProfileAlreadyActiveException extends RuntimeException{
    public ProfileAlreadyActiveException(String message) {
        super(message);
    }
}

package com.example.notificationservice.Exeptions;

public class RessourceAlreadyEnabledException extends RuntimeException {
    public RessourceAlreadyEnabledException(String message) {
        super(message);
    }
}

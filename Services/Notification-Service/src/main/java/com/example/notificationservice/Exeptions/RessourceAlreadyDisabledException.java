package com.example.notificationservice.Exeptions;

public class RessourceAlreadyDisabledException extends RuntimeException {
    public RessourceAlreadyDisabledException(String message) {
        super(message);
    }
}

package com.example.notificationservice.Exeptions;

public class InvalidNotificationTimeException extends RuntimeException {
    public InvalidNotificationTimeException(String message) {
        super(message);
    }
}


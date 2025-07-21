package com.example.adminservice.Config.Exceptions;

public class RessourceAlreadyEnabledException extends RuntimeException {
    public RessourceAlreadyEnabledException(String message) {
        super(message);
    }
}

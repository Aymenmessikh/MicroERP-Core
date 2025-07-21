package com.example.adminservice.Config.Exceptions;

public class AuthorityAlreadyExistsException extends RuntimeException {
    public AuthorityAlreadyExistsException(String message) {
        super(message);
    }
}

package com.example.adminservice.Config.Exceptions;

public class MyResourceNotFoundException extends RuntimeException{
    public MyResourceNotFoundException(String message) {
        super(message);
    }
}

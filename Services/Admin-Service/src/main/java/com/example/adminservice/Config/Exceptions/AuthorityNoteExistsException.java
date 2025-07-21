package com.example.adminservice.Config.Exceptions;

public class AuthorityNoteExistsException extends RuntimeException{
    public AuthorityNoteExistsException(String message){
        super(message);
    }
}

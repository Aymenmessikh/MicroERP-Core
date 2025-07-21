package com.example.adminservice.Controller;

import com.example.adminservice.Config.Exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(MyResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(MyResourceNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ModuleMismatchException.class)
    public ResponseEntity<String> handleModuleMismatchException(ModuleMismatchException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(AuthorityAlreadyExistsException.class)
    public ResponseEntity<String> handleAuthorityAlreadyExistsException(AuthorityAlreadyExistsException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(RessourceAlreadyDisabledException.class)
    public ResponseEntity<String> handleRessourceAlreadyDisabledException(RessourceAlreadyDisabledException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(RessourceAlreadyEnabledException.class)
    public ResponseEntity<String> handleRessourceAlreadyEnabledException(RessourceAlreadyEnabledException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    @ExceptionHandler(AuthorityNoteExistsException.class)
    public ResponseEntity<String> handleAuthorityNoteExistesExeption(AuthorityNoteExistsException exception){
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<String> handleUserCreationException(UserCreationException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + exception.getMessage());
    }
}

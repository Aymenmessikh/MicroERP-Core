package com.example.notificationservice.Controller;

import com.example.notificationservice.Exeptions.InvalidNotificationTimeException;
import com.example.notificationservice.Exeptions.MyResourceNotFoundException;
import com.example.notificationservice.Exeptions.RessourceAlreadyDisabledException;
import com.example.notificationservice.Exeptions.RessourceAlreadyEnabledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(MyResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(MyResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
    @ExceptionHandler(RessourceAlreadyDisabledException.class)
    public ResponseEntity<String> handleRessourceAlreadyDisabledException(RessourceAlreadyDisabledException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(RessourceAlreadyEnabledException.class)
    public ResponseEntity<String> handleRessourceAlreadyEnabledException(RessourceAlreadyEnabledException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    @ExceptionHandler(InvalidNotificationTimeException.class)
    public ResponseEntity<String> handleInvalidNotificationTimeException
            (InvalidNotificationTimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}

package com.example.librarymanagement.ExceptionHandling;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class HandleException {

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred.");
    }

    @ExceptionHandler(InvalidBookIdException.class)
    public static ResponseEntity<String> handleInvalidBookIdException(InvalidBookIdException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public static ResponseEntity<String> handleInvalidUserIdException(InvalidUserIdException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}




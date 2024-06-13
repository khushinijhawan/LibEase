package com.example.librarymanagement.ExceptionHandling;

public class InvalidBookIdException extends Exception {

    public InvalidBookIdException(String message) {
        super(message);
    }
}

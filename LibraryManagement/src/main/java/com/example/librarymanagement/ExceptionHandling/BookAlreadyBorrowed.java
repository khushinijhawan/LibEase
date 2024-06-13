package com.example.librarymanagement.ExceptionHandling;

public class BookAlreadyBorrowed extends RuntimeException{
    public BookAlreadyBorrowed(String message) {
        super(message);
    }
}

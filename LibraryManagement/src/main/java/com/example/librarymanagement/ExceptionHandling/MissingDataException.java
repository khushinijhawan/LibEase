package com.example.librarymanagement.ExceptionHandling;

public class MissingDataException extends RuntimeException{
    public MissingDataException(String m)
    {
        super(m);
    }
}

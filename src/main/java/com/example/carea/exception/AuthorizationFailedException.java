package com.example.carea.exception;

public class AuthorizationFailedException extends RuntimeException
{
    public AuthorizationFailedException(String message)
    {
        super(message);
    }
}
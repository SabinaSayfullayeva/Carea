package com.example.carea.exception;

public class NoUniqueNameException extends RuntimeException
{
    public NoUniqueNameException(String lang)
    {
        super(lang);
    }
}

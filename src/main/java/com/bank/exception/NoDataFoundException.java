package com.bank.exception;

public class NoDataFoundException extends RuntimeException{
    public NoDataFoundException(String message)
    {
        super(message);
    }
}

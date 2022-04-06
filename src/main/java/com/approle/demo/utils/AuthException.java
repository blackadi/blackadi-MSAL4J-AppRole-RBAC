package com.approle.demo.utils;

public class AuthException extends RuntimeException {

    public AuthException(String message, Throwable cause){
        super(message, cause);
    }
}
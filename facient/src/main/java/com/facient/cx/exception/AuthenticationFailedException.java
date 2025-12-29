package com.facient.cx.exception;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message) {

        super(message);
    }
}

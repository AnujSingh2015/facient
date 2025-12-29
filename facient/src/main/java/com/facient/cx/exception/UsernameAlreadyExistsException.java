package com.facient.cx.exception;


public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {

        super("Username '" + username + "' already exists.");
    }
}


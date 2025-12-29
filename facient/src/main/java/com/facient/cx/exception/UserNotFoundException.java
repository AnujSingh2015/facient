package com.facient.cx.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {

        super("Username '" + username + "' doesn't exists.");
    }
}

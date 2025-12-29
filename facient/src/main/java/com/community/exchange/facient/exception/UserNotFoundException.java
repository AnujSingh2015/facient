package com.community.exchange.facient.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {

        super("Username '" + username + "' doesn't exists.");
    }
}

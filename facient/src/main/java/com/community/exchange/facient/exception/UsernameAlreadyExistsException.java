package com.community.exchange.facient.exception;


public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {

        super("Username '" + username + "' already exists.");
    }
}


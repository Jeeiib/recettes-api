package com.recettes.api.exceptions;

public class EmailDejaExistantException extends RuntimeException {

    public EmailDejaExistantException(String message) {
        super(message);
    }
}

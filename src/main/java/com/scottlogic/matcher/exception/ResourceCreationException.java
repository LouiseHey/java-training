package com.scottlogic.matcher.exception;

public class ResourceCreationException extends RuntimeException {
    public ResourceCreationException(String message, Exception e) {
        super(String.format("Exception occurred when creating a resource: %s, %s", message, e.getMessage()), e);
    }
}

package com.scottlogic.matcher.controller;

import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidArgument(MethodArgumentNotValidException e) {
        return "Constraint Violation: " + e.getMessage();
    }

    @ExceptionHandler(MongoWriteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateResource(MongoWriteException e) {
        return "Resource already exists: " + e.getMessage();
    }
}

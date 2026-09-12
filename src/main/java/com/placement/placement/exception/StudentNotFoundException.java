package com.placement.placement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {

    // Default constructor
    public StudentNotFoundException() {
        super("Student not found");
    }

    // Constructor with custom message
    public StudentNotFoundException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
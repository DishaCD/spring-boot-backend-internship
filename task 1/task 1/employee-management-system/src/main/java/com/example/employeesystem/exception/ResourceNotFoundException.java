package com.example.employeesystem.exception;

/**
 * Thrown when a requested resource (e.g. employee) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

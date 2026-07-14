package com.example.employeesystem.exception;

/**
 * Thrown when an operation would violate the unique email constraint.
 * Mapped to HTTP 409 Conflict by the global exception handler.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}

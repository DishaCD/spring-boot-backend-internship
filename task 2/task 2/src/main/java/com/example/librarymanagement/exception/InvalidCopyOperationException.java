package com.example.librarymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCopyOperationException extends RuntimeException {
    public InvalidCopyOperationException(String message) {
        super(message);
    }
}

package com.example.bakery.exceptions;

public class InvalidProductImageException extends RuntimeException {
    public InvalidProductImageException(String message) {
        super(message);
    }
}

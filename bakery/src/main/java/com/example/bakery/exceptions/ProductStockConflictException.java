package com.example.bakery.exceptions;

public class ProductStockConflictException extends RuntimeException {
    public ProductStockConflictException(String message) {
        super(message);
    }
}

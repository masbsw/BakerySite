package com.example.OrderBakery.exceptions;

public class StockConflictException extends RuntimeException {
    public StockConflictException(String message) {
        super(message);
    }
}

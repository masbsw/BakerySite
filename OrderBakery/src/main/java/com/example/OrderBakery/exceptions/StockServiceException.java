package com.example.OrderBakery.exceptions;

public class StockServiceException extends RuntimeException {
    public StockServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

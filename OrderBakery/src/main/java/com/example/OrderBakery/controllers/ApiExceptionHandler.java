package com.example.OrderBakery.controllers;

import com.example.OrderBakery.exceptions.StockConflictException;
import com.example.OrderBakery.exceptions.StockServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Ошибка валидации заказа");
        response.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(StockConflictException.class)
    public ResponseEntity<Map<String, Object>> handleStockConflict(StockConflictException exception) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(StockServiceException.class)
    public ResponseEntity<Map<String, Object>> handleStockServiceException(StockServiceException exception) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Не удалось обновить остатки товаров. Попробуйте позже.");

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }
}

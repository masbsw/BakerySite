package com.example.bakery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StockDecreaseResponse {
    private String message;
    private List<StockDecreaseItemResponse> items;
}

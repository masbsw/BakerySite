package com.example.bakery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDecreaseItemResponse {
    private Long productId;
    private String productName;
    private Integer reservedQuantity;
    private Integer remainingQuantity;
}

package com.example.OrderBakery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDecreaseItemRequest {
    private Long productId;
    private Integer quantity;
}

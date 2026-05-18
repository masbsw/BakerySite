package com.example.bakery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockDecreaseItemRequest {
    @NotNull(message = "productId обязателен")
    private Long productId;

    @NotNull(message = "quantity обязательен")
    @Min(value = 1, message = "quantity должен быть больше 0")
    private Integer quantity;
}

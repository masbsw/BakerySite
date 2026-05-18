package com.example.OrderBakery.DTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull(message = "Товар обязателен")
    private Long productId;

    @Min(value = 1, message = "Количество товара должно быть больше 0")
    private int productQuantity;
}

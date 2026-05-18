package com.example.bakery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class StockDecreaseRequest {
    @Valid
    @NotEmpty(message = "Список товаров не должен быть пустым")
    private List<StockDecreaseItemRequest> items;
}

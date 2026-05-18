package com.example.OrderBakery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StockDecreaseRequest {
    private List<StockDecreaseItemRequest> items;
}

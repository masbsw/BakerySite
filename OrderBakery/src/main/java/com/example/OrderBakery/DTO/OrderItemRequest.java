package com.example.OrderBakery.DTO;


import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private int productQuantity;
}

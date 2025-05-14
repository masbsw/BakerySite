package com.example.OrderBakery.DTO;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequest {
    private String customerName;
    private String customerPhone;
    private String deliveryMethod;
    private String deliveryAddress;
    private LocalDate deliveryDate;
    private String orderComment;
    private List<OrderItemRequest> items;
}

package com.example.OrderBakery.DTO;


import com.example.OrderBakery.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderResponse {
    private Long orderId;
    private String customerName;
    private String customerPhone;
    private String deliveryMethod;
    private String deliveryAddress;
    private LocalDate deliveryDate;
    private String orderComment;
    private OrderStatus orderStatus;
    private LocalDateTime orderCreatedAt;
    private List<OrderItemResponse> orderItems;
}

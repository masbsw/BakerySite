package com.example.OrderBakery.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column (name = "delivery_method")
    private String deliveryMethod;

    @Column(name = "delivery_address")
    private  String deliveryAddress;

    @Column (name = "delivery_date")
    private LocalDate deliveryDate;

    @Column (name = "order_comment")
    private String orderComment;

    @Enumerated(EnumType.STRING)
    @Column (name = "order_status")
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column (name = "order_created_at")
    private LocalDateTime orderCreatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;



}

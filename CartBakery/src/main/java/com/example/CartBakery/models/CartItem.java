package com.example.CartBakery.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_item")
public class CartItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "item_session_id")
    private String itemSessionId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_quantity")
    private int itemQuantity;

}

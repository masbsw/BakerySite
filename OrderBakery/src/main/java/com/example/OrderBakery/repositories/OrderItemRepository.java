package com.example.OrderBakery.repositories;

import com.example.OrderBakery.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

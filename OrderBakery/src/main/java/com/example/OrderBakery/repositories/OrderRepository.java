package com.example.OrderBakery.repositories;


import com.example.OrderBakery.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

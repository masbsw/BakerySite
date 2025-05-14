package com.example.OrderBakery.services;


import com.example.OrderBakery.DTO.OrderItemResponse;
import com.example.OrderBakery.DTO.OrderResponse;
import com.example.OrderBakery.models.Order;
import com.example.OrderBakery.models.OrderStatus;
import com.example.OrderBakery.repositories.OrderItemRepository;
import com.example.OrderBakery.repositories.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public Order addOrder(Order order){
        Order saveOrder = orderRepository.save(order);

        if (order.getOrderItems() != null){
            order.getOrderItems().forEach(item -> {
                item.setOrder(saveOrder);
                orderItemRepository.save(item);
            });
        }
        return saveOrder;
    }

    @Transactional(readOnly = true)
    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToOrderResponse);
    }

    private OrderResponse convertToOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getOrderItemId(),
                        item.getProductId(),
                        item.getProductQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getDeliveryMethod(),
                order.getDeliveryAddress(),
                order.getDeliveryDate(),
                order.getOrderComment(),
                order.getOrderStatus(),
                order.getOrderCreatedAt(),
                items
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    List<OrderItemResponse> items = order.getOrderItems().stream()
                            .map(item -> new OrderItemResponse(
                                    item.getOrderItemId(),
                                    item.getProductId(),
                                    item.getProductQuantity()
                            ))
                            .collect(Collectors.toList());

                    return new OrderResponse(
                            order.getOrderId(),
                            order.getCustomerName(),
                            order.getCustomerPhone(),
                            order.getDeliveryMethod(),
                            order.getDeliveryAddress(),
                            order.getDeliveryDate(),
                            order.getOrderComment(),
                            order.getOrderStatus(),
                            order.getOrderCreatedAt(),
                            items
                    );
                })
                .collect(Collectors.toList());
    }

    public Order updateStatusOrder(Long id, OrderStatus newStatus){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found"));

        order.setOrderStatus(newStatus);
        return orderRepository.save(order);

    }
}

package com.example.OrderBakery.controllers;


import com.example.OrderBakery.DTO.OrderItemResponse;
import com.example.OrderBakery.DTO.OrderRequest;
import com.example.OrderBakery.DTO.OrderResponse;
import com.example.OrderBakery.models.Order;
import com.example.OrderBakery.models.OrderItem;
import com.example.OrderBakery.models.OrderStatus;
import com.example.OrderBakery.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request){
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setDeliveryAddress(request.getDeliveryMethod().equals("pickup") ? "Самовывоз" : request.getDeliveryAddress());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setOrderComment(request.getOrderComment());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = request.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(item.getProductId());
                    orderItem.setProductQuantity(item.getProductQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);

        Order saveOrder = orderService.addOrder(order);

        OrderResponse response = MapToOrderResponse(saveOrder);
        return ResponseEntity.ok(response);
    }

    private OrderResponse MapToOrderResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setDeliveryMethod(order.getDeliveryMethod());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setDeliveryDate(order.getDeliveryDate());
        response.setOrderComment(order.getOrderComment());
        response.setOrderStatus(order.getOrderStatus());
        response.setOrderCreatedAt(order.getOrderCreatedAt());

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(this::MapToOrderItemResponse)
                .collect(Collectors.toList());
        response.setOrderItems(itemResponses);

        return response;
    }

    private OrderItemResponse MapToOrderItemResponse(OrderItem item){
        OrderItemResponse response = new OrderItemResponse();

        response.setOrderItemId(item.getOrderItemId());
        response.setProductId(item.getProductId());
        response.setProductQuantity(item.getProductQuantity());

        return response;
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public Order updateStatusOrder(@PathVariable Long id, @RequestParam OrderStatus status){
        return orderService.updateStatusOrder(id, status);
    }
}

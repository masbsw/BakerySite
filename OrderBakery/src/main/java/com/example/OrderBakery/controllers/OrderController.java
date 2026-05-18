package com.example.OrderBakery.controllers;

import com.example.OrderBakery.DTO.OrderRequest;
import com.example.OrderBakery.DTO.OrderResponse;
import com.example.OrderBakery.models.Order;
import com.example.OrderBakery.models.OrderItem;
import com.example.OrderBakery.models.OrderStatus;
import com.example.OrderBakery.security.JwtAuthenticatedUser;
import com.example.OrderBakery.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            @AuthenticationPrincipal JwtAuthenticatedUser authenticatedUser
    ){
        Order order = new Order();
        order.setUserId(authenticatedUser.getUserId());
        order.setUsername(authenticatedUser.getUsername());
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

        Order saveOrder = orderService.createOrder(order);

        OrderResponse response = orderService.buildOrderResponse(saveOrder);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<OrderResponse> updateStatusOrder(@PathVariable Long id, @RequestParam OrderStatus status){
        return ResponseEntity.ok(orderService.updateStatusOrder(id, status));
    }
}

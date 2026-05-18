package com.example.OrderBakery.services;


import com.example.OrderBakery.DTO.OrderItemResponse;
import com.example.OrderBakery.DTO.OrderResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.OrderBakery.dto.StockDecreaseItemRequest;
import com.example.OrderBakery.dto.StockDecreaseRequest;
import com.example.OrderBakery.exceptions.StockConflictException;
import com.example.OrderBakery.exceptions.StockServiceException;
import com.example.OrderBakery.models.Order;
import com.example.OrderBakery.models.OrderStatus;
import com.example.OrderBakery.repositories.OrderItemRepository;
import com.example.OrderBakery.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${catalog.service.url:http://localhost:8082/api}")
    private String catalogServiceUrl;

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

    public Order createOrder(Order order) {
        decreaseProductStock(order);
        return addOrder(order);
    }

    @Transactional(readOnly = true)
    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::buildOrderResponse);
    }

    public OrderResponse buildOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getOrderItemId(),
                        item.getProductId(),
                        item.getProductQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getUsername(),
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
                .map(this::buildOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateStatusOrder(Long id, OrderStatus newStatus){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found"));

        order.setOrderStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        return buildOrderResponse(savedOrder);

    }

    private void decreaseProductStock(Order order) {
        List<StockDecreaseItemRequest> items = order.getOrderItems().stream()
                .map(item -> new StockDecreaseItemRequest(item.getProductId(), item.getProductQuantity()))
                .collect(Collectors.toList());

        StockDecreaseRequest request = new StockDecreaseRequest(items);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    catalogServiceUrl + "/products/decrease-stock",
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    Void.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new StockServiceException("Не удалось обновить остатки товаров.", null);
            }
        } catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode().value() == 409) {
                String message = exception.getResponseBodyAsString();
                throw new StockConflictException(extractMessage(message));
            }
            throw new StockServiceException("Не удалось обновить остатки товаров.", exception);
        } catch (RestClientException exception) {
            throw new StockServiceException("Не удалось обновить остатки товаров.", exception);
        }
    }

    private String extractMessage(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "Недостаточно товара на складе.";
        }

        try {
            String message = objectMapper.readValue(responseBody, new TypeReference<java.util.Map<String, String>>() {})
                    .get("message");
            return message == null || message.isBlank() ? responseBody : message;
        } catch (Exception ignored) {
            return responseBody;
        }
    }
}

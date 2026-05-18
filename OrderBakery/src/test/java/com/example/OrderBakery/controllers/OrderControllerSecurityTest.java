package com.example.OrderBakery.controllers;

import com.example.OrderBakery.DTO.OrderResponse;
import com.example.OrderBakery.exceptions.StockConflictException;
import com.example.OrderBakery.models.Order;
import com.example.OrderBakery.models.OrderStatus;
import com.example.OrderBakery.services.OrderService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerSecurityTest {

    private static final String TEST_SECRET = "YWRtaW5iYWtlcnktand0LXNlY3JldC1rZXktZm9yLXRlc3RzLW9yZGVyLTIwMjY=";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void createOrderWithoutTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderBody()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createOrderWithBearerTokenReturnsSavedOrder() throws Exception {
        Order order = new Order();
        order.setOrderId(15L);
        order.setUserId(7L);
        order.setUsername("buyer");
        order.setCustomerName("Иван");
        order.setCustomerPhone("+79990001122");
        order.setDeliveryMethod("pickup");
        order.setDeliveryAddress("Самовывоз");
        order.setDeliveryDate(LocalDate.of(2026, 5, 20));
        order.setOrderComment("Комментарий");
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderCreatedAt(LocalDateTime.of(2026, 5, 17, 12, 0));
        order.setOrderItems(List.of());

        given(orderService.createOrder(any(Order.class))).willReturn(order);

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + buildToken(7L, "buyer", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(15))
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.username").value("buyer"))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"));
    }

    @Test
    void createOrderWithInvalidPayloadReturnsBadRequestAndDoesNotCreateOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + buildToken(7L, "buyer", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "И",
                                  "customerPhone": "12345",
                                  "deliveryMethod": "pickup",
                                  "deliveryDate": "2020-01-01",
                                  "items": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ошибка валидации заказа"))
                .andExpect(jsonPath("$.fieldErrors.customerName").exists())
                .andExpect(jsonPath("$.fieldErrors.customerPhone").exists())
                .andExpect(jsonPath("$.fieldErrors.deliveryDate").exists())
                .andExpect(jsonPath("$.fieldErrors.items").exists());

        then(orderService).should(never()).createOrder(any(Order.class));
    }

    @Test
    void createOrderWhenStockIsInsufficientReturnsConflict() throws Exception {
        given(orderService.createOrder(any(Order.class)))
                .willThrow(new StockConflictException("Недостаточно товара \"Круассан\" на складе. Доступно: 1, запрошено: 3."));

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + buildToken(7L, "buyer", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderBody()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Недостаточно товара \"Круассан\" на складе. Доступно: 1, запрошено: 3."));
    }

    @Test
    void updateOrderStatusReturnsOrderResponseDto() throws Exception {
        OrderResponse response = new OrderResponse();
        response.setOrderId(15L);
        response.setUserId(7L);
        response.setUsername("buyer");
        response.setCustomerName("Иван");
        response.setCustomerPhone("+79990001122");
        response.setDeliveryMethod("pickup");
        response.setDeliveryAddress("Самовывоз");
        response.setDeliveryDate(LocalDate.of(2026, 5, 20));
        response.setOrderStatus(OrderStatus.COMPLETED);
        response.setOrderCreatedAt(LocalDateTime.of(2026, 5, 17, 12, 0));
        response.setOrderItems(List.of());

        given(orderService.updateStatusOrder(15L, OrderStatus.COMPLETED)).willReturn(response);

        mockMvc.perform(put("/api/orders/15/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(15))
                .andExpect(jsonPath("$.orderStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.username").value("buyer"))
                .andExpect(jsonPath("$.order.orderId").doesNotExist());
    }

    private String validOrderBody() {
        return """
                {
                  "customerName": "Иван",
                  "customerPhone": "+79990001122",
                  "deliveryMethod": "pickup",
                  "deliveryAddress": "Самовывоз",
                  "deliveryDate": "2026-05-20",
                  "orderComment": "Комментарий",
                  "items": [
                    {
                      "productId": 1,
                      "productQuantity": 2
                    }
                  ]
                }
                """;
    }

    private String buildToken(Long userId, String username, String role) {
        SecretKey key = Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(TEST_SECRET));
        Date now = new Date();
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

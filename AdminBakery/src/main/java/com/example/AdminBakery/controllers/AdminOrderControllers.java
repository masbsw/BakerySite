package com.example.AdminBakery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderControllers {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @GetMapping
    public ResponseEntity<String> getAllOrders() {
        String url = orderServiceUrl + "/orders";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) {
        String url = orderServiceUrl + "/orders/" + id;
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        String url = orderServiceUrl + "/orders/" + id + "/status?status=" + status;
        return restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
    }
}


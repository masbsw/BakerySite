package com.example.AdminBakery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${catalog.service.url}")
    private String catalogServiceUrl;

    @GetMapping
    public ResponseEntity<String> getAllProducts() {
        String url = catalogServiceUrl + "/products";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) {
        String url = catalogServiceUrl + "/products/" + id;
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String productJson) {
        String url = catalogServiceUrl + "/products";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(productJson, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody String productJson) {
        String url = catalogServiceUrl + "/products/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(productJson, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        String url = catalogServiceUrl + "/products/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }
}
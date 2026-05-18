package com.example.AdminBakery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return jsonResponse(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) {
        String url = catalogServiceUrl + "/products/" + id;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return jsonResponse(response);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String productJson) {
        String url = catalogServiceUrl + "/products";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(productJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return jsonResponse(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody String productJson) {
        String url = catalogServiceUrl + "/products/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(productJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        return jsonResponse(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        String url = catalogServiceUrl + "/products/" + id;
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProductImage(@RequestParam("file") MultipartFile file) throws IOException {
        String url = catalogServiceUrl + "/products/upload-image";

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType(
                file.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : file.getContentType()
        ));

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileResource, fileHeaders);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    String.class
            );
            return jsonResponse(response);
        } catch (HttpStatusCodeException exception) {
            return ResponseEntity.status(exception.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exception.getResponseBodyAsString());
        }
    }

    private ResponseEntity<String> jsonResponse(ResponseEntity<String> upstreamResponse) {
        return ResponseEntity.status(upstreamResponse.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(upstreamResponse.getBody());
    }
}

package com.example.gateway.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String ORDER_SERVICE_URL = "http://localhost:8089/orders";

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody String orderRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(orderRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    ORDER_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}

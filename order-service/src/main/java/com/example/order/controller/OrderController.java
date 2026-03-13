package com.example.order.controller;

import com.example.order.event.OrderEvent;
import com.example.order.model.Order;
import com.example.order.service.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        // Generate unique order ID
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);

        // Log order creation
        System.out.println("Order created: " + order);

        // Create event and publish to Kafka
        OrderEvent event = new OrderEvent(order.getOrderId(), order.getProduct(), order.getQuantity());
        orderProducer.sendOrderEvent(event);

        return ResponseEntity.ok("Order created with ID: " + orderId);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Service is running!");
    }
}

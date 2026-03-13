package com.example.billing.service;

import com.example.billing.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BillingConsumer {

    @KafkaListener(topics = "order-topic", groupId = "billing-group")
    public void consumeOrderEvent(OrderEvent event) {
        System.out.println("Billing Service received OrderEvent: " + event);
        System.out.println("Generating invoice for order: " + event.getOrderId());

        // Simulate invoice generation logic
        double amount = event.getQuantity() * 100.0; // Assume $100 per item
        System.out.println("Invoice generated for order " + event.getOrderId() + " - Amount: $" + amount);
    }
}

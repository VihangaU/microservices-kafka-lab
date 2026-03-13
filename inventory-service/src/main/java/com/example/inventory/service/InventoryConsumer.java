package com.example.inventory.service;

import com.example.inventory.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consumeOrderEvent(OrderEvent event) {
        System.out.println("Inventory Service received OrderEvent: " + event);
        System.out.println(
                "Updating inventory for product: " + event.getProduct() + ", quantity: " + event.getQuantity());

        // Simulate inventory update logic
        System.out.println("Stock updated successfully for order: " + event.getOrderId());
    }
}

package com.ecommerce.microcommerce.service;

import java.lang.System.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventListenerService {
    
    private static final Logger logger = LoggerFactory.getLogger(EventListenerService.class);
    
    @KafkaListener(topics = "client-events", groupId = "orders-service")
    public void handleClientEvent(Object clientEvent) {
        logger.info("Received client event in orders service: {}", clientEvent);
        // Here you can add business logic to handle client events
        // For example: validate existing orders when client is updated/deleted
    }
    
    @KafkaListener(topics = "product-events", groupId = "orders-service")
    public void handleProductEvent(Object productEvent) {
        logger.info("Received product event in orders service: {}", productEvent);
        // Here you can add business logic to handle product events
        // For example: update order totals when product prices change
    }
}

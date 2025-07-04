package com.ecommerce.microcommerce.service;

import com.ecommerce.microcommerce.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String ORDER_CREATED_TOPIC = "order-created";

    @Autowired
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        try {
            logger.info("Publishing order created event: {}", event);
            kafkaTemplate.send(ORDER_CREATED_TOPIC, String.valueOf(event.getOrderId()), event);
            logger.info("Order created event published successfully for order ID: {}", event.getOrderId());
        } catch (Exception e) {
            logger.error("Failed to publish order created event for order ID: {}", event.getOrderId(), e);
        }
    }
}

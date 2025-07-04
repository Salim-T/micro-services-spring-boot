package com.ecommerce.microcommerce.service;

import com.ecommerce.microcommerce.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "order-created", groupId = "clients-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        logger.info("Received order created event in clients service: {}", event);

        // Ici vous pourriez ajouter de la logique spécifique aux clients
        // Par exemple : mettre à jour le statut client, historique des commandes, etc.

        logger.info("Processed order created event for client ID: {}", event.getClientId());
    }
}

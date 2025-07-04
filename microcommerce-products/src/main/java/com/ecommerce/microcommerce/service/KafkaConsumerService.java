package com.ecommerce.microcommerce.service;

import com.ecommerce.microcommerce.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "order-created", groupId = "products-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        logger.info("Received order created event in products service: {}", event);

        // Ici vous pourriez ajouter de la logique spécifique aux produits
        // Par exemple : mettre à jour le stock, statistiques de vente, etc.

        logger.info("Processed order created event for products: {}", event.getProductIds());
    }
}

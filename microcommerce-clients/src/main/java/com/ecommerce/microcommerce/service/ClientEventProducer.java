package main.java.com.ecommerce.microcommerce.service;

import java.lang.System.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import main.java.com.ecommerce.microcommerce.event.ClientEvent;

@Service
public class ClientEventProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientEventProducer.class);
    private static final String TOPIC = "client-events";
    
    @Autowired
    private KafkaTemplate<String, ClientEvent> kafkaTemplate;
    
    public void publishClientEvent(ClientEvent clientEvent) {
        try {
            logger.info("Publishing client event: {}", clientEvent);
            kafkaTemplate.send(TOPIC, String.valueOf(clientEvent.getClientId()), clientEvent);
            logger.info("Client event published successfully");
        } catch (Exception e) {
            logger.error("Error publishing client event: {}", e.getMessage(), e);
        }
    }
}

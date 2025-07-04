package com.ecommerce.microcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.microcommerce.event.ClientEvent;

@Service
public class ClientEventProducer {
    
    private static final String TOPIC = "client-events";
    
    @Autowired
    private KafkaTemplate<String, ClientEvent> kafkaTemplate;
    
    public void publishClientEvent(ClientEvent clientEvent) {
        try {
            System.out.println("Publishing client event: " + clientEvent);
            kafkaTemplate.send(TOPIC, String.valueOf(clientEvent.getClientId()), clientEvent);
            System.out.println("Client event published successfully");
        } catch (Exception e) {
            System.err.println("Error publishing client event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

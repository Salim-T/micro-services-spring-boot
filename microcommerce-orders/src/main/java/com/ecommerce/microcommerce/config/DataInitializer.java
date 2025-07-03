package com.ecommerce.microcommerce.config;

import com.ecommerce.microcommerce.model.Order;
import com.ecommerce.microcommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        // Check if the database is empty
        if (orderRepository.count() == 0) {
            // Initialize with some sample data
            orderRepository.save(new Order("Order 1", 100.0));
            orderRepository.save(new Order("Order 2", 200.0));
            orderRepository.save(new Order("Order 3", 300.0));
        }
    }
}

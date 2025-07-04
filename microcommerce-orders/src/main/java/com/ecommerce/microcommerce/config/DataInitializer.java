package com.ecommerce.microcommerce.config;

import com.ecommerce.microcommerce.model.Order;
import com.ecommerce.microcommerce.model.OrderProduct;
import com.ecommerce.microcommerce.repository.OrderRepository;
import com.ecommerce.microcommerce.repository.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public void run(String... args) {
        // Check if the database is empty
        if (orderRepository.count() == 0) {
            // Initialize with some sample data

            // Order 1
            Order order1 = new Order("Sample Order 1", 1);
            order1.setTotal(BigDecimal.valueOf(150.0));
            order1 = orderRepository.save(order1);

            OrderProduct op1 = new OrderProduct(order1.getId(), 1, 2, BigDecimal.valueOf(50.0));
            OrderProduct op2 = new OrderProduct(order1.getId(), 2, 1, BigDecimal.valueOf(50.0));
            orderProductRepository.save(op1);
            orderProductRepository.save(op2);

            // Order 2
            Order order2 = new Order("Sample Order 2", 2);
            order2.setTotal(BigDecimal.valueOf(100.0));
            order2 = orderRepository.save(order2);

            OrderProduct op3 = new OrderProduct(order2.getId(), 2, 2, BigDecimal.valueOf(50.0));
            orderProductRepository.save(op3);

            System.out.println("Database initialized with sample orders.");
        }
    }
}

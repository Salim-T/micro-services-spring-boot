package com.ecommerce.microcommerce.service;

import com.ecommerce.microcommerce.model.Order;
import com.ecommerce.microcommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    public List<Order> searchOrders(String keyword) {
        return orderRepository.findByDescriptionContaining(keyword);
    }

    public boolean orderExists(int id) {
        return orderRepository.existsById(id);
    }
}

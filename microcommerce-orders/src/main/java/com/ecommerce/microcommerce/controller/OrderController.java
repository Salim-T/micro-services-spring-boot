package com.ecommerce.microcommerce.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.microcommerce.service.OrderService;
import com.ecommerce.microcommerce.model.Order;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<Order> listOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable int id, @RequestBody Order orderDetails) {
        if (!orderService.orderExists(id)) {
            return ResponseEntity.notFound().build();
        }

        Order updated = orderService.saveOrder(orderDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        if (!orderService.orderExists(id)) {
            return ResponseEntity.notFound().build();
        }
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/search")
    public List<Order> searchOrders(@RequestParam String keyword) {
        return orderService.searchOrders(keyword);
    }
}

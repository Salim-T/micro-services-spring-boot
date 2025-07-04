package com.ecommerce.microcommerce.repository;

import com.ecommerce.microcommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByDescriptionContaining(String keyword);

    List<Order> findByClientId(int clientId);
}

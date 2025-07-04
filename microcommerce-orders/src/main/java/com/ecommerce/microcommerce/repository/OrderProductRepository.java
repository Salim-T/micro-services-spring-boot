package com.ecommerce.microcommerce.repository;

import com.ecommerce.microcommerce.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    List<OrderProduct> findByOrderId(int orderId);

    List<OrderProduct> findByProductId(int productId);

    void deleteByOrderId(int orderId);
}

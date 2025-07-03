package com.ecommerce.microcommerce.repository;

import com.ecommerce.microcommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Méthodes de base héritées de JpaRepository :
    // - findAll()
    // - findById(Integer id)
    // - save(Product product)
    // - deleteById(Integer id)

    // Méthodes personnalisées si nécessaire
    List<Product> findByNameContainingIgnoreCase(String name);

    // @Query("SELECT p FROM Product p WHERE p.price > :price")
    // List<Product> findProductsMoreExpensiveThan(@Param("price") double price);
}

package com.ecommerce.microcommerce.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.microcommerce.service.ProductService;
import com.ecommerce.microcommerce.model.Product;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> listProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product productDetails) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product updatedProduct = productService.saveProduct(productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

}

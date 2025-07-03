package com.ecommerce.microcommerce.config;

import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // Ne s'exécute pas dans les tests
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Ne peupler que si la base est vide
        if (productRepository.count() == 0) {
            System.out.println("Initialisation des données d'exemple...");

            productRepository.save(new Product("Product A", 29.99));
            productRepository.save(new Product("Product B", 49.99));
            productRepository.save(new Product("Product C", 19.99));
            productRepository.save(new Product("Laptop Dell XPS", 999.99));
            productRepository.save(new Product("iPhone 15", 799.99));
            productRepository.save(new Product("Samsung Galaxy S24", 699.99));
            productRepository.save(new Product("MacBook Pro", 1299.99));
            productRepository.save(new Product("iPad Air", 549.99));
            productRepository.save(new Product("AirPods Pro", 249.99));
            productRepository.save(new Product("Apple Watch", 399.99));

            System.out.println("Données d'exemple initialisées avec succès !");
        } else {
            System.out.println("Base de données déjà peuplée, pas d'initialisation nécessaire.");
        }
    }
}

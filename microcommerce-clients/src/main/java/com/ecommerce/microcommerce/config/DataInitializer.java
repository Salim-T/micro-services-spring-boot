package com.ecommerce.microcommerce.config;

import com.ecommerce.microcommerce.model.Client;
import com.ecommerce.microcommerce.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // Ne s'exécute pas dans les tests
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception {
        // Ne peupler que si la base est vide
        if (clientRepository.count() == 0) {
            System.out.println("Initialisation des données d'exemple...");

            clientRepository.save(new Client("John", "Doe", "john.doe@example.com", "123 Main St", "Springfield", "USA",
                    "1234567890"));
            clientRepository.save(new Client("Alice", "Smith", "alice.smith@example.com", "456 Elm St", "Springfield",
                    "USA", "0987654321"));
            clientRepository.save(new Client("Bob", "Johnson", "bob.johnson@example.com", "789 Oak St", "Springfield",
                    "USA", "1122334455"));

            System.out.println("Données d'exemple initialisées avec succès !");
        } else {
            System.out.println("Base de données déjà peuplée, pas d'initialisation nécessaire.");
        }
    }
}

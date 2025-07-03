package com.ecommerce.microcommerce.repository;

import com.ecommerce.microcommerce.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testSaveAndFindClient() {
        // Given
        Client client = new Client("John", "Doe", "john.doe@example.com", "123 Main St", "Springfield", "USA",
                "1234567890");

        // When
        Client savedClient = clientRepository.save(client);

        // Then
        assertThat(savedClient.getId()).isNotNull();
        assertThat(savedClient.getFirstname()).isEqualTo("John");
        assertThat(savedClient.getLastname()).isEqualTo("Doe");
        assertThat(savedClient.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedClient.getAddress()).isEqualTo("123 Main St");
        assertThat(savedClient.getCity()).isEqualTo("Springfield");
        assertThat(savedClient.getCountry()).isEqualTo("USA");
        assertThat(savedClient.getPhone()).isEqualTo("1234567890");
    }

    @Test
    void testFindAll() {
        // Given
        clientRepository.save(new Client("Alice", "Smith", "alice.smith@example.com", "456 Elm St", "Springfield",
                "USA", "0987654321"));
        clientRepository.save(new Client("Bob", "Johnson", "bob.johnson@example.com", "789 Oak St", "Springfield",
                "USA", "1122334455"));

        // When
        List<Client> clients = clientRepository.findAll();

        // Then
        assertThat(clients).hasSize(2);
    }

    @Test
    void testFindByNameContaining() {
        // Given
        clientRepository.save(new Client("Charlie", "Brown", "charlie.brown@example.com", "123 Maple St", "Springfield",
                "USA", "1231231234"));
        clientRepository.save(new Client("Diana", "Prince", "diana.prince@example.com", "456 Birch St", "Springfield",
                "USA", "9876543210"));
        clientRepository.save(new Client("Eve", "Adams", "eve.adams@example.com", "789 Cedar St", "Springfield", "USA",
                "5555555555"));

        // When
        List<Client> clients = clientRepository.findByFirstnameContainingIgnoreCase("eve");

        // Then
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getFirstname()).isEqualTo("Eve");
    }

    @Test
    void testDeleteById() {
        // Given
        Client client = clientRepository.save(new Client("To Delete", "User", "to.delete@example.com", "123 Main St",
                "Springfield", "USA", "1234567890"));
        Integer clientId = client.getId();

        // When
        clientRepository.deleteById(clientId);

        // Then
        Optional<Client> deletedClient = clientRepository.findById(clientId);
        assertThat(deletedClient).isEmpty();
    }
}

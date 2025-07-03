package com.ecommerce.microcommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.microcommerce.model.Client;
import com.ecommerce.microcommerce.repository.ClientRepository;

import main.java.com.ecommerce.microcommerce.event.ClientEvent;
import main.java.com.ecommerce.microcommerce.service.ClientEventProducer;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientEventProducer clientEventProducer;

    public ClientService(ClientRepository clientRepository, ClientEventProducer clientEventProducer) {
        this.clientRepository = clientRepository;
        this.clientEventProducer = clientEventProducer;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(int id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        boolean isNewClient = client.getId() == 0;
        Client savedClient = clientRepository.save(client);
        
        // Create and publish event
        String eventType = isNewClient ? "CREATED" : "UPDATED";
        ClientEvent clientEvent = new ClientEvent(
            eventType,
            savedClient.getId(),
            savedClient.getFirstname(),
            savedClient.getLastname(),
            savedClient.getEmail(),
            savedClient.getAddress(),
            savedClient.getCity(),
            savedClient.getCountry(),
            savedClient.getPhone()
        );
        
        clientEventProducer.publishClientEvent(clientEvent);
        return savedClient;
    }

    public void deleteClient(int id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            clientRepository.deleteById(id);
            
            // Create and publish delete event
            ClientEvent clientEvent = new ClientEvent(
                "DELETED",
                client.getId(),
                client.getFirstname(),
                client.getLastname(),
                client.getEmail(),
                client.getAddress(),
                client.getCity(),
                client.getCountry(),
                client.getPhone()
            );
            
            clientEventProducer.publishClientEvent(clientEvent);
        }
    }

    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByFirstnameContainingIgnoreCase(name);
    }

    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }
}

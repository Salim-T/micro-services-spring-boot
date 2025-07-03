package com.ecommerce.microcommerce.service;

import com.ecommerce.microcommerce.model.Client;
import com.ecommerce.microcommerce.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(int id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(int id) {
        clientRepository.deleteById(id);
    }

    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByFirstnameContainingIgnoreCase(name);
    }

    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }
}

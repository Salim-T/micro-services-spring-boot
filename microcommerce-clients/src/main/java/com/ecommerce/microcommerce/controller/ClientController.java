package com.ecommerce.microcommerce.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.microcommerce.service.ClientService;
import com.ecommerce.microcommerce.model.Client;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public List<Client> listClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable int id) {
        Optional<Client> product = clientService.getClientById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/clients")
    public Client createProduct(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable int id, @RequestBody Client productDetails) {
        if (!clientService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Client updated = clientService.saveClient(productDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        if (!clientService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clients/search")
    public List<Client> searchProducts(@RequestParam String name) {
        return clientService.searchClientsByName(name);
    }

}

package com.ecommerce.microcommerce.service;

import com.ecommerce.microcommerce.dto.ClientDTO;
import com.ecommerce.microcommerce.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalClientService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalClientService.class);

    private final WebClient webClient;

    @Value("${clients.service.url}")
    private String clientsServiceUrl;

    @Value("${products.service.url}")
    private String productsServiceUrl;

    public ExternalClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public ClientDTO getClientById(int clientId) {
        try {
            logger.info("Fetching client with ID: {}", clientId);
            return webClient.get()
                    .uri(clientsServiceUrl + "/api/clients/" + clientId)
                    .retrieve()
                    .bodyToMono(ClientDTO.class)
                    .block();
        } catch (Exception e) {
            logger.error("Failed to fetch client with ID: {}", clientId, e);
            return null;
        }
    }

    public ProductDTO getProductById(int productId) {
        try {
            logger.info("Fetching product with ID: {}", productId);
            return webClient.get()
                    .uri(productsServiceUrl + "/api/products/" + productId)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
        } catch (Exception e) {
            logger.error("Failed to fetch product with ID: {}", productId, e);
            return null;
        }
    }

    public List<ProductDTO> getProductsByIds(List<Integer> productIds) {
        return productIds.stream()
                .map((Integer id) -> getProductById(id))
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }

    public boolean clientExists(int clientId) {
        return getClientById(clientId) != null;
    }

    public boolean allProductsExist(List<Integer> productIds) {
        return productIds.stream()
                .allMatch(id -> getProductById(id) != null);
    }
}

package com.ecommerce.microcommerce.dto;

import java.util.List;

public class CreateOrderRequest {
    private int clientId;
    private List<OrderProductRequest> products;
    private String description;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(int clientId, List<OrderProductRequest> products, String description) {
        this.clientId = clientId;
        this.products = products;
        this.description = description;
    }

    // Getters and Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public List<OrderProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductRequest> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "clientId=" + clientId +
                ", products=" + products +
                ", description='" + description + '\'' +
                '}';
    }
}

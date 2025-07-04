package com.ecommerce.microcommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCreatedEvent {
    private int orderId;
    private int clientId;
    private List<Integer> productIds;
    private double total;
    private String description;
    private LocalDateTime timestamp;

    public OrderCreatedEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public OrderCreatedEvent(int orderId, int clientId, List<Integer> productIds, double total, String description) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.productIds = productIds;
        this.total = total;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId=" + orderId +
                ", clientId=" + clientId +
                ", productIds=" + productIds +
                ", total=" + total +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

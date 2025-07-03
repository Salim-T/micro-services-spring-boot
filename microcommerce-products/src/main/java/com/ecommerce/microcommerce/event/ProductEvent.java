package com.ecommerce.microcommerce.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProductEvent {
    private String eventType; // CREATED, UPDATED, DELETED
    private int productId;
    private String name;
    private double price;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public ProductEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public ProductEvent(String eventType, int productId, String name, double price) {
        this.eventType = eventType;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ProductEvent{" +
                "eventType='" + eventType + '\'' +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}

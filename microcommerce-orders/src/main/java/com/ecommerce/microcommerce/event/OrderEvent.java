package main.java.com.ecommerce.microcommerce.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderEvent {
    private String eventType; // CREATED, UPDATED, DELETED
    private int orderId;
    private String description;
    private double total;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public OrderEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public OrderEvent(String eventType, int orderId, String description, double total) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.description = description;
        this.total = total;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "eventType='" + eventType + '\'' +
                ", orderId=" + orderId +
                ", description='" + description + '\'' +
                ", total=" + total +
                ", timestamp=" + timestamp +
                '}';
    }
}

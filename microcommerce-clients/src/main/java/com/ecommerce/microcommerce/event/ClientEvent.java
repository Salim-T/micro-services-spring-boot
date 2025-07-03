package main.java.com.ecommerce.microcommerce.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientEvent {
    private String eventType; // CREATED, UPDATED, DELETED
    private int clientId;
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String city;
    private String country;
    private String phone;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public ClientEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public ClientEvent(String eventType, int clientId, String firstname, String lastname, 
                      String email, String address, String city, String country, String phone) {
        this.eventType = eventType;
        this.clientId = clientId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ClientEvent{" +
                "eventType='" + eventType + '\'' +
                ", clientId=" + clientId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

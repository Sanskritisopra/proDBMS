package com.example.demo.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ClientPhone")
@EqualsAndHashCode
@NoArgsConstructor // Add no-arg constructor
public class ClientPhone {

    @EmbeddedId
    private ClientPhoneId id; // Composite key

    @ManyToOne
    @JoinColumn(name = "ClientId", insertable = false, updatable = false)
    private Client client; // Reference to the User entity

    // Constructor for convenience
    public ClientPhone(Client client, String phoneNumber) {
        this.client = client;
        this.id = new ClientPhoneId(client.getClientId(), phoneNumber);
    }

    // Optional: Override toString for easier debugging
    @Override
    public String toString() {
        return "ClientPhone{" +
                "ClientId=" + id.getClientId() +
                ", phoneNumber='" + id.getPhoneNumber() + '\'' +
                '}';
    }

    public Object getPhoneNumber() {
        return null;
    }
}

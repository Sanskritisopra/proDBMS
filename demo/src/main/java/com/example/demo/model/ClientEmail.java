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
@Table(name = "ClientEmail")
@EqualsAndHashCode
@NoArgsConstructor // Add no-arg constructor
public class ClientEmail {

    @EmbeddedId
    private ClientEmailId id; // Composite key

    @ManyToOne
    @JoinColumn(name = "ClientId", insertable = false, updatable = false)
    private Client client; // Reference to the User entity

    // Constructor for convenience
    public ClientEmail(Client client, String email) {
        this.client = client;
        this.id = new ClientEmailId(client.getClientId(), email);
    }

    // Optional: Override toString for easier debugging
    @Override
    public String toString() {
        return "ClientEmail{" +
                "ClientId=" + id.getClientId() +
                ", email='" + id.getemailAddress() + '\'' +
                '}';
    }

    public Object getemail() {
        return id.getemailAddress();
    }
}

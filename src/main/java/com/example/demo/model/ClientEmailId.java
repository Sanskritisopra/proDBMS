package com.example.demo.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ClientEmailId implements Serializable {
    private Integer ClientId;
    private String emailAddress;
    public Integer getClientId() {
        return ClientId;
    }

    public String getemailAddress() {
        return emailAddress;
    }
}


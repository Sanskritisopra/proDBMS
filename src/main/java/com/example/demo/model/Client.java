package com.example.demo.model;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Transactional
@Data
@NoArgsConstructor
@Table(name = "Client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ClientId;

    @Column(name = "FName", length = 50)
    private String fName;

    @Column(name = "MName", length = 50)
    private String mName;

    @Column(name = "LName", length = 50)
    private String lName;

    @Column(name = "Occupation", length = 100)
    private String occupation;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Spouse", length = 100)
    private String spouse;

    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "children")
    private String children;

    // @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    // private Set<ClientEmail> clientEmails;

    // @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    // private Set<ClientPhone> clientPhones;

}


package com.example.demo.model;

import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Transactional
@Data
@NoArgsConstructor
@Table(name = "CorporateCase")
public class CorporateCase {

    public CorporateCase(int int1, String string, java.sql.Date date, java.sql.Date date2, int int2, String string2) {
    }

    public CorporateCase(int int1, String string, java.sql.Date date, java.sql.Date date2, Client client2,
            String string2) {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int corporateCaseID;

    @Column(name = "CaseDesc", columnDefinition = "TEXT")
    private String caseDesc;

    @Column(name = "StartDate", nullable = false)
    private String startDate;

    @Column(name = "EndDate")
    private String endDate;

    @ManyToOne // Many CorporateCases can belong to one Client
    @JoinColumn(name = "ClientId", nullable = false)
    private Client client;

    @Column(name = "CaseStatus", length = 50)
    private String caseStatus;

    
}

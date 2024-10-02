package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Transactional
@Data
@NoArgsConstructor
@Table(name = "InvoiceCorp")
public class InvoiceCorp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceID;

    @OneToOne
    @JoinColumn(name = "CorporateCaseID", referencedColumnName = "CorporateCaseID", nullable = false)
    private CorporateCase corporateCase;

    @Temporal(TemporalType.DATE)
    private Date invoiceDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(length = 50)
    private String status;

    // Getters and Setters
    
}

package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "AppointmentCorp")
public class AppointmentCorp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentID;

    @ManyToOne
    @JoinColumn(name = "CorporateCaseID", nullable = false)
    private CorporateCase corporateCase;

    @ManyToOne
    @JoinColumn(name = "LawyerID", nullable = false)
    private Lawyer lawyer;

    @ManyToOne
    @JoinColumn(name = "ClientID", nullable = false)
    private Client client;

    @Column(name = "AppointmentDate", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "AppointmentTime", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "Location", length = 50)
    private String location;

    public void setCorporateCaseID(int int1) {
    }

    public void setLawyerID(int int1) {
    }

    public void setClientID(int int1) {
    }

    public int getCorporateCaseID() {
        return corporateCase != null ? corporateCase.getCorporateCaseID() : 0; // or return null if preferred
    }
    
    public int getLawyerID() {
        return lawyer != null ? lawyer.getLawyerID() : 0; // or return null if preferred
    }
    
    public int getClientID() {
        return client != null ? client.getClientId() : 0; // or return null if preferred
    }
}

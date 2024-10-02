package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Lawyer")
public class Lawyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lawyerID;

    @Column(name = "FName", length = 50, nullable = false)
    private String fname;

    @Column(name = "MName", length = 50)
    private String mname;

    @Column(name = "LName", length = 50, nullable = false)
    private String lname;

    @Column(name = "DateOfBirth", nullable = false)
    private LocalDate dob;

    @Column(name = "Qualification", length = 100)
    private String qualification;

    @Column(name = "Experience")
    private int experience;

    @Column(name = "Positions", length = 50)
    private String position;

    @Column(name = "Expertise", length = 50)
    private String expertise;

    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Column(name = "Email", length = 100)
    private String email;

    public boolean isApproved() {
        return false;
    }
}

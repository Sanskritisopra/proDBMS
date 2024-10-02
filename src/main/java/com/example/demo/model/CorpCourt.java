package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "CorpCourt")
public class CorpCourt {

    @Id
    @Column(name = "CourtCorpID")
    private int courtCorpID;

    @Column(name = "CourtName", length = 255)
    private String courtName;

    @Column(name = "CourtPincode", length = 20)
    private String courtPincode;

    @Column(name = "CourtState", length = 100)
    private String courtState;

    @Column(name = "CourtCity", length = 100)
    private String courtCity;

    @Column(name = "FJName", length = 50)
    private String fjName;

    @Column(name = "MJName", length = 50)
    private String mjName;

    @Column(name = "LJName", length = 50)
    private String ljName;

    // Getters and Setters
    public int getCourtCorpID() {
        return courtCorpID;
    }

    public void setCourtCorpID(int courtCorpID) {
        this.courtCorpID = courtCorpID;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getCourtPincode() {
        return courtPincode;
    }

    public void setCourtPincode(String courtPincode) {
        this.courtPincode = courtPincode;
    }

    public String getCourtState() {
        return courtState;
    }

    public void setCourtState(String courtState) {
        this.courtState = courtState;
    }

    public String getCourtCity() {
        return courtCity;
    }

    public void setCourtCity(String courtCity) {
        this.courtCity = courtCity;
    }

    public String getFjName() {
        return fjName;
    }

    public void setFjName(String fjName) {
        this.fjName = fjName;
    }

    public String getMjName() {
        return mjName;
    }

    public void setMjName(String mjName) {
        this.mjName = mjName;
    }

    public String getLjName() {
        return ljName;
    }

    public void setLjName(String ljName) {
        this.ljName = ljName;
    }
}

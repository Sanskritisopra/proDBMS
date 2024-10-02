package com.example.demo.model;

import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "TaskCorp")
public class TaskCorp {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskID;

    @ManyToOne
    @JoinColumn(name = "CorporateCaseID", referencedColumnName = "CorporateCaseID", nullable = false)
    private CorporateCase corporateCase;

    @Column(name = "TaskDesc")
    private String taskDesc;

    @Column(name = "Status")
    private String status;

    @Column(name = "DueDate")
    private LocalDate dueDate;

    // Getters and Setters
    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public CorporateCase getCorporateCase() {
        return corporateCase;
    }

    public void setCorporateCase(CorporateCase corporateCase) {
        this.corporateCase = corporateCase;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

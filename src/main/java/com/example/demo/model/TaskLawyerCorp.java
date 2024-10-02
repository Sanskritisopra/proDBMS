package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TaskLawyerCorp")
public class TaskLawyerCorp {

    @Id
    @ManyToOne
    @JoinColumn(name = "TaskID", referencedColumnName = "TaskID", nullable = false)
    private TaskCorp task;

    @Id
    @ManyToOne
    @JoinColumn(name = "LawyerID", referencedColumnName = "LawyerID", nullable = false)
    private Lawyer lawyer;

    @Id
    @ManyToOne
    @JoinColumn(name = "CorporateCaseID", referencedColumnName = "CorporateCaseID", nullable = false)
    private CorporateCase corporateCase;

    // Constructors
    public TaskLawyerCorp() {}

    public TaskLawyerCorp(TaskCorp task, Lawyer lawyer, CorporateCase corporateCase) {
        this.task = task;
        this.lawyer = lawyer;
        this.corporateCase = corporateCase;
    }

    // Getters and Setters
    public TaskCorp getTask() {
        return task;
    }

    public void setTask(TaskCorp task) {
        this.task = task;
    }

    public Lawyer getLawyer() {
        return lawyer;
    }

    public void setLawyer(Lawyer lawyer) {
        this.lawyer = lawyer;
    }

    public CorporateCase getCorporateCase() {
        return corporateCase;
    }

    public void setCorporateCase(CorporateCase corporateCase) {
        this.corporateCase = corporateCase;
    }
}

package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TaskParalegalCorp")
public class TaskParalegalCorp {

    @Id
    @ManyToOne
    @JoinColumn(name = "TaskID", referencedColumnName = "TaskID", nullable = false)
    private TaskCorp task;

    @Id
    @ManyToOne
    @JoinColumn(name = "ParalegalID", referencedColumnName = "ParalegalID", nullable = false)
    private Paralegal paralegal;

    @Id
    @ManyToOne
    @JoinColumn(name = "CorporateCaseID", referencedColumnName = "CorporateCaseID", nullable = false)
    private CorporateCase corporateCase;

    // Constructors
    public TaskParalegalCorp() {}

    public TaskParalegalCorp(TaskCorp task, Paralegal paralegal , CorporateCase corporateCase) {
        this.task = task;
        this.paralegal = paralegal;
        this.corporateCase = corporateCase;
    }

    // Getters and Setters
    public TaskCorp getTask() {
        return task;
    }

    public void setTask(TaskCorp task) {
        this.task = task;
    }

    public Paralegal getParalegal() {
        return paralegal;
    }

    public void setParalegal(Paralegal paralegal) {
        this.paralegal = paralegal;
    }

    public CorporateCase getCorporateCase() {
        return corporateCase;
    }

    public void setCorporateCase(CorporateCase corporateCase) {
        this.corporateCase = corporateCase;
    }
}

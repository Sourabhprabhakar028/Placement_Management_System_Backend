package com.placement.placement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Applied date cannot be null")
    private LocalDate appliedDate;

    private String remarks;

    @JsonIgnore
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewRound> interviewRounds = new ArrayList<>();

    public JobApplication() {}

    public JobApplication(Student student, Company company,
                          String status, LocalDate appliedDate, String remarks) {
        this.student = student;
        this.company = company;
        this.status = status;
        this.appliedDate = appliedDate;
        this.remarks = remarks;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public List<InterviewRound> getInterviewRounds() { return interviewRounds; }
    public void setInterviewRounds(List<InterviewRound> interviewRounds) { this.interviewRounds = interviewRounds; }
}
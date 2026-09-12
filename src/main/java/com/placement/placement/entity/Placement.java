package com.placement.placement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "placements")
public class Placement {

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

    @NotNull(message = "Placement date cannot be null")
    private LocalDate placementDate;

    @NotNull(message = "CTC cannot be null")
    private Double ctc;

    public Placement() {}

    // ✅ 5-arg constructor — matches PlacementServiceImpl
    public Placement(Student student, Company company, String status,
                     LocalDate placementDate, Double ctc) {
        this.student = student;
        this.company = company;
        this.status = status;
        this.placementDate = placementDate;
        this.ctc = ctc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getPlacementDate() { return placementDate; }
    public void setPlacementDate(LocalDate placementDate) { this.placementDate = placementDate; }
    public Double getCtc() { return ctc; }
    public void setCtc(Double ctc) { this.ctc = ctc; }
}
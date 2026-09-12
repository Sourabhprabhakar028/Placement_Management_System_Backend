package com.placement.placement.dto.response;

import java.time.LocalDate;

public class PlacementResponse {

    private Long id;
    private StudentResponse student;
    private CompanyResponse company;
    private String status;
    private LocalDate placementDate;
    private Double ctc;

    public PlacementResponse() {}

    // ✅ Constructor used by PlacementServiceImpl
    public PlacementResponse(Long id, StudentResponse student,
                             CompanyResponse company, String status,
                             LocalDate placementDate, Double ctc) {
        this.id = id;
        this.student = student;
        this.company = company;
        this.status = status;
        this.placementDate = placementDate;
        this.ctc = ctc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StudentResponse getStudent() { return student; }
    public void setStudent(StudentResponse student) { this.student = student; }
    public CompanyResponse getCompany() { return company; }
    public void setCompany(CompanyResponse company) { this.company = company; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getPlacementDate() { return placementDate; }
    public void setPlacementDate(LocalDate placementDate) { this.placementDate = placementDate; }
    public Double getCtc() { return ctc; }
    public void setCtc(Double ctc) { this.ctc = ctc; }
}
package com.placement.placement.dto.response;

import java.time.LocalDate;

public class JobApplicationResponse {

    private Long id;
    private StudentResponse student;
    private CompanyResponse company;
    private String status;
    private LocalDate appliedDate;
    private String remarks;

    public JobApplicationResponse() {}

    public JobApplicationResponse(Long id, StudentResponse student,
                                  CompanyResponse company, String status,
                                  LocalDate appliedDate, String remarks) {
        this.id = id;
        this.student = student;
        this.company = company;
        this.status = status;
        this.appliedDate = appliedDate;
        this.remarks = remarks;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentResponse getStudent() { return student; }
    public void setStudent(StudentResponse student) { this.student = student; }

    public CompanyResponse getCompany() { return company; }
    public void setCompany(CompanyResponse company) { this.company = company; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
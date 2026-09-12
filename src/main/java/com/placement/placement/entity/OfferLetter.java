package com.placement.placement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "offer_letters")
public class OfferLetter {

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

    private String filePath;
    private LocalDate uploadedDate;
    private Double ctc;

    public OfferLetter() {}

    public OfferLetter(Student student, Company company,
                       String filePath, LocalDate uploadedDate, Double ctc) {
        this.student = student;
        this.company = company;
        this.filePath = filePath;
        this.uploadedDate = uploadedDate;
        this.ctc = ctc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public LocalDate getUploadedDate() { return uploadedDate; }
    public void setUploadedDate(LocalDate uploadedDate) { this.uploadedDate = uploadedDate; }
    public Double getCtc() { return ctc; }
    public void setCtc(Double ctc) { this.ctc = ctc; }
}
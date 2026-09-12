package com.placement.placement.dto.response;

import java.time.LocalDate;

public class OfferLetterResponse {

    private Long id;
    private String studentName;
    private String companyName;
    private String filePath;
    private LocalDate uploadedDate;
    private Double ctc;

    public OfferLetterResponse() {}

    public OfferLetterResponse(Long id, String studentName,
                               String companyName, String filePath,
                               LocalDate uploadedDate, Double ctc) {
        this.id = id;
        this.studentName = studentName;
        this.companyName = companyName;
        this.filePath = filePath;
        this.uploadedDate = uploadedDate;
        this.ctc = ctc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDate getUploadedDate() { return uploadedDate; }
    public void setUploadedDate(LocalDate uploadedDate) { this.uploadedDate = uploadedDate; }

    public Double getCtc() { return ctc; }
    public void setCtc(Double ctc) { this.ctc = ctc; }
}
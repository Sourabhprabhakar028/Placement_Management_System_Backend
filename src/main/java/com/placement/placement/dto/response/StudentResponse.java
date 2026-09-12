package com.placement.placement.dto.response;

public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private String branch;
    private Double percentage;
    private String resumePath;
    private String profilePicturePath;

    public StudentResponse() {}

    public StudentResponse(Long id, String name, String email,
                           String branch, Double percentage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.percentage = percentage;
    }

    // ✅ 7-arg constructor used by PlacementServiceImpl
    public StudentResponse(Long id, String name, String email,
                           String branch, Double percentage,
                           String resumePath, String profilePicturePath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.percentage = percentage;
        this.resumePath = resumePath;
        this.profilePicturePath = profilePicturePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
    public String getProfilePicturePath() { return profilePicturePath; }
    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }
}
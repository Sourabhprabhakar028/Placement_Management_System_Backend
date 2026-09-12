package com.placement.placement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Branch cannot be empty")
    private String branch;

    @NotNull(message = "Percentage is required")
    @Min(value = 0, message = "Percentage must be >= 0")
    @Max(value = 100, message = "Percentage must be <= 100")
    private Double percentage;

    private String resumePath;

    private String profilePicturePath;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> jobApplications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Placement> placements = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfferLetter> offerLetters = new ArrayList<>();

    // ✅ interviewRounds REMOVED — InterviewRound maps to JobApplication, not Student

    public Student() {}

    public Student(String name, String email, String branch, Double percentage) {
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.percentage = percentage;
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

    public List<JobApplication> getJobApplications() { return jobApplications; }
    public void setJobApplications(List<JobApplication> jobApplications) { this.jobApplications = jobApplications; }

    public List<Placement> getPlacements() { return placements; }
    public void setPlacements(List<Placement> placements) { this.placements = placements; }

    public List<OfferLetter> getOfferLetters() { return offerLetters; }
    public void setOfferLetters(List<OfferLetter> offerLetters) { this.offerLetters = offerLetters; }
}

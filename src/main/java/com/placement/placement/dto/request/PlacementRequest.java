package com.placement.placement.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PlacementRequest {

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Placement date cannot be null")
    private LocalDate placementDate;

    @NotNull(message = "CTC cannot be null")
    @Min(value = 0, message = "CTC must be >= 0")
    private Double ctc;

    public PlacementRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getPlacementDate() { return placementDate; }
    public void setPlacementDate(LocalDate placementDate) { this.placementDate = placementDate; }
    public Double getCtc() { return ctc; }
    public void setCtc(Double ctc) { this.ctc = ctc; }
}
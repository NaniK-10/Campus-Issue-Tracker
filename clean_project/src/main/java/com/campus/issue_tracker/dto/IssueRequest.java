package com.campus.issue_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IssueRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String location;

    private Double latitude;
    private Double longitude;

    // ✅ NEW: anonymous flag
    private boolean anonymous;

    private com.campus.issue_tracker.entity.IssueCategory category;

    // Optional category specific fields
    private String courseUnit;
    private String paymentId;
    private String hostelBlock;
    private String roomNumber;
}

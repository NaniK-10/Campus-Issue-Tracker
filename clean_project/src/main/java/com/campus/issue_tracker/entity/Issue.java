package com.campus.issue_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private IssueStatus status = IssueStatus.PENDING;

    @Column(nullable = false)
    private boolean anonymous = false;

    @Enumerated(EnumType.STRING)
    private IssueCategory category;

    // Category specific fields
    private String courseUnit; // For ACADEMIC
    private String paymentId; // For PAYMENTS
    private String hostelBlock; // For HOSTEL
    private String roomNumber; // For HOSTEL

    // The Relationship: Many issues belong to one reporter (User)
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String attachmentPath;

    @Column(length = 1000)
    private String studentFeedback;

    private Integer rating;

    // ✅ AUTO ESCALATION FIELDS
    private LocalDateTime escalationTime;

    @Column(nullable = false)
    private boolean escalated = false;

    @Column(nullable = false)
    private int escalationLevel = 0;
    // ⏱️ last escalation happened time
    private LocalDateTime lastEscalatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

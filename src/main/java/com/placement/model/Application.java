package com.placement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    private Long id;
    private Long studentId;
    private Long jobId;
    private String status;        // APPLIED / SHORTLISTED / INTERVIEW / SELECTED / REJECTED
    private Integer matchScore;
    private String notes;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    // joined fields for convenience
    private String studentName;
    private String studentEmail;
    private String jobTitle;
    private String companyName;
}

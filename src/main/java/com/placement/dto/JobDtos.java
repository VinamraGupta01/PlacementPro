package com.placement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class JobDtos {

    @Data
    public static class JobCreateRequest {
        private Long companyId;
        private String title;
        private String description;
        private Double minCgpa;
        private String eligibleBranches; // CSV
        private Integer passingYear;
        private String jobType;
        private String location;
        private LocalDate deadline;
        private List<String> requiredSkills;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class MatchScoreResponse {
        private Integer matchScore;
        private List<String> matchedSkills;
        private List<String> missingSkills;
        private boolean eligible;
        private String eligibilityReason;
    }
}

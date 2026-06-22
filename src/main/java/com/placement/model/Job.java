package com.placement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    private Long id;
    private Long companyId;
    private String companyName;       // joined
    private String title;
    private String description;
    private Double minCgpa;
    private String eligibleBranches;  // CSV: "CSE,IT,ECE"
    private Integer passingYear;
    private String jobType;           // INTERNSHIP / FULL_TIME
    private String location;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private List<String> requiredSkills;
}

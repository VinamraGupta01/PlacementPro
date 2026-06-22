package com.placement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String branch;
    private Double cgpa;
    private Integer passingYear;
    private String phone;
    private String resumeUrl;
    private String role;
    private LocalDateTime createdAt;
    private List<String> skills;
}

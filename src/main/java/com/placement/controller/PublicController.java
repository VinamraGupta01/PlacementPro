package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.model.Company;
import com.placement.model.Job;
import com.placement.service.CompanyService;
import com.placement.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Public endpoints used by the landing page (no auth needed).
 */
@RestController
@RequestMapping("/api/public")
@Tag(name = "Public", description = "No-auth endpoints for landing page")
public class PublicController {

    private final JobService jobService;
    private final CompanyService companyService;

    public PublicController(JobService j, CompanyService c) {
        this.jobService = j; this.companyService = c;
    }

    @GetMapping("/jobs")
    @Operation(summary = "Public list of open jobs")
    public ApiResponse<List<Job>> jobs() { return ApiResponse.ok(jobService.getAll()); }

    @GetMapping("/companies")
    @Operation(summary = "Public list of companies")
    public ApiResponse<List<Company>> companies() { return ApiResponse.ok(companyService.getAll()); }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.ok(Map.of("status", "UP"));
    }
}

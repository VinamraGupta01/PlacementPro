package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.JobDtos;
import com.placement.model.Application;
import com.placement.model.Job;
import com.placement.service.ApplicationService;
import com.placement.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Jobs", description = "Job/Internship listings, eligibility & applications")
public class JobController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    public JobController(JobService j, ApplicationService a) {
        this.jobService = j; this.applicationService = a;
    }

    @GetMapping
    @Operation(summary = "List all jobs")
    public ApiResponse<List<Job>> all() { return ApiResponse.ok(jobService.getAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get a job by id")
    public ApiResponse<Job> one(@PathVariable Long id) { return ApiResponse.ok(jobService.getById(id)); }

    @PostMapping
    @Operation(summary = "Create a job/internship (admin)")
    public ApiResponse<Job> create(@RequestBody JobDtos.JobCreateRequest req) {
        return ApiResponse.ok("Job created", jobService.create(req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a job (admin)")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ApiResponse.ok("Job deleted", null);
    }

    @GetMapping("/eligible/{studentId}")
    @Operation(summary = "Get all jobs the given student is eligible for")
    public ApiResponse<List<Job>> eligible(@PathVariable Long studentId) {
        return ApiResponse.ok(jobService.eligibleJobsForStudent(studentId));
    }

    @GetMapping("/{jobId}/match/{studentId}")
    @Operation(summary = "Smart eligibility & skill-match score for a student vs. a job")
    public ApiResponse<JobDtos.MatchScoreResponse> matchScore(@PathVariable Long jobId, @PathVariable Long studentId) {
        return ApiResponse.ok(jobService.matchForStudent(studentId, jobId));
    }

    @PostMapping("/{jobId}/apply/{studentId}")
    @Operation(summary = "Apply to a job")
    public ApiResponse<Application> apply(@PathVariable Long jobId, @PathVariable Long studentId) {
        return ApiResponse.ok("Application submitted", applicationService.apply(studentId, jobId));
    }

    @GetMapping("/{jobId}/applicants")
    @Operation(summary = "List all applicants for a job (admin)")
    public ApiResponse<List<Application>> applicants(@PathVariable Long jobId) {
        return ApiResponse.ok(applicationService.findByJob(jobId));
    }
}

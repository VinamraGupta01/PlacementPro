package com.placement.service;

import com.placement.dto.JobDtos;
import com.placement.exception.ApiException;
import com.placement.model.Application;
import com.placement.model.Job;
import com.placement.model.Student;
import com.placement.repository.ApplicationRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ApplicationService {

    private static final Set<String> VALID_STATUSES =
            Set.of("APPLIED", "SHORTLISTED", "INTERVIEW", "SELECTED", "REJECTED");

    private final ApplicationRepository appRepo;
    private final StudentRepository studentRepo;
    private final JobRepository jobRepo;
    private final JobService jobService;

    public ApplicationService(ApplicationRepository a, StudentRepository s,
                              JobRepository j, JobService js) {
        this.appRepo = a; this.studentRepo = s; this.jobRepo = j; this.jobService = js;
    }

    public Application apply(Long studentId, Long jobId) {
        Student s = studentRepo.findById(studentId).orElseThrow(() -> ApiException.notFound("Student not found"));
        s.setSkills(studentRepo.findSkills(studentId));
        Job j = jobRepo.findById(jobId).orElseThrow(() -> ApiException.notFound("Job not found"));
        j.setRequiredSkills(jobRepo.findRequiredSkills(jobId));

        if (appRepo.existsByStudentAndJob(studentId, jobId)) {
            throw ApiException.conflict("You have already applied to this job");
        }

        JobDtos.MatchScoreResponse score = jobService.isEligible(s, j);
        if (!score.isEligible()) {
            throw ApiException.badRequest("Not eligible: " + score.getEligibilityReason());
        }

        Application app = Application.builder()
                .studentId(studentId).jobId(jobId).status("APPLIED")
                .matchScore(score.getMatchScore()).build();
        Long id = appRepo.save(app);
        return appRepo.findById(id).orElseThrow();
    }

    public Application updateStatus(Long applicationId, String status) {
        String up = status == null ? "" : status.toUpperCase();
        if (!VALID_STATUSES.contains(up)) {
            throw ApiException.badRequest("Invalid status. Allowed: " + VALID_STATUSES);
        }
        appRepo.findById(applicationId).orElseThrow(() -> ApiException.notFound("Application not found"));
        appRepo.updateStatus(applicationId, up);
        return appRepo.findById(applicationId).orElseThrow();
    }

    public Application updateNotes(Long applicationId, String notes) {
        appRepo.findById(applicationId).orElseThrow(() -> ApiException.notFound("Application not found"));
        appRepo.updateNotes(applicationId, notes);
        return appRepo.findById(applicationId).orElseThrow();
    }

    public List<Application> findByStudent(Long studentId) { return appRepo.findByStudent(studentId); }
    public List<Application> findByJob(Long jobId) { return appRepo.findByJob(jobId); }
    public List<Application> findAll() { return appRepo.findAll(); }
    public Application findById(Long id) {
        return appRepo.findById(id).orElseThrow(() -> ApiException.notFound("Application not found"));
    }
}

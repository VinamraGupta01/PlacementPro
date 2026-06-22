package com.placement.service;

import com.placement.dto.JobDtos;
import com.placement.exception.ApiException;
import com.placement.model.Job;
import com.placement.model.Student;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepo;
    private final CompanyRepository companyRepo;
    private final StudentRepository studentRepo;

    public JobService(JobRepository j, CompanyRepository c, StudentRepository s) {
        this.jobRepo = j; this.companyRepo = c; this.studentRepo = s;
    }

    public Job create(JobDtos.JobCreateRequest req) {
        if (req.getCompanyId() == null || companyRepo.findById(req.getCompanyId()).isEmpty())
            throw ApiException.badRequest("Valid companyId is required");
        if (req.getTitle() == null || req.getTitle().isBlank())
            throw ApiException.badRequest("Title is required");

        Job j = Job.builder()
                .companyId(req.getCompanyId()).title(req.getTitle()).description(req.getDescription())
                .minCgpa(req.getMinCgpa() == null ? 0.0 : req.getMinCgpa())
                .eligibleBranches(req.getEligibleBranches())
                .passingYear(req.getPassingYear()).jobType(req.getJobType())
                .location(req.getLocation()).deadline(req.getDeadline())
                .build();
        Long id = jobRepo.save(j);
        if (req.getRequiredSkills() != null) {
            req.getRequiredSkills().stream()
                    .filter(s -> s != null && !s.isBlank()).map(String::trim).distinct()
                    .forEach(s -> jobRepo.addRequiredSkill(id, s));
        }
        return getById(id);
    }

    public Job getById(Long id) {
        Job j = jobRepo.findById(id).orElseThrow(() -> ApiException.notFound("Job not found"));
        j.setRequiredSkills(jobRepo.findRequiredSkills(id));
        return j;
    }

    public List<Job> getAll() {
        List<Job> all = jobRepo.findAll();
        all.forEach(j -> j.setRequiredSkills(jobRepo.findRequiredSkills(j.getId())));
        return all;
    }

    public void delete(Long id) { jobRepo.delete(id); }

    /**
     * Lists every job that the given student is eligible for, with match-score info.
     */
    public List<Job> eligibleJobsForStudent(Long studentId) {
        Student s = studentRepo.findById(studentId).orElseThrow(() -> ApiException.notFound("Student not found"));
        s.setSkills(studentRepo.findSkills(studentId));
        return getAll().stream()
                .filter(j -> isEligible(s, j).isEligible())
                .collect(Collectors.toList());
    }

    /**
     * Smart eligibility checker.
     */
    public JobDtos.MatchScoreResponse isEligible(Student student, Job job) {
        // CGPA check
        if (job.getMinCgpa() != null && student.getCgpa() != null && student.getCgpa() < job.getMinCgpa()) {
            return failed("CGPA below " + job.getMinCgpa());
        }
        // Branch check
        if (job.getEligibleBranches() != null && !job.getEligibleBranches().isBlank()) {
            Set<String> branches = Arrays.stream(job.getEligibleBranches().split(","))
                    .map(String::trim).map(String::toUpperCase).collect(Collectors.toSet());
            if (student.getBranch() == null || !branches.contains(student.getBranch().toUpperCase())) {
                return failed("Branch not eligible (allowed: " + job.getEligibleBranches() + ")");
            }
        }
        // Year check
        if (job.getPassingYear() != null && student.getPassingYear() != null
                && !job.getPassingYear().equals(student.getPassingYear())) {
            return failed("Passing year mismatch (required: " + job.getPassingYear() + ")");
        }
        // Skill match
        JobDtos.MatchScoreResponse score = computeMatch(student.getSkills(), job.getRequiredSkills());
        score.setEligible(true);
        score.setEligibilityReason("Eligible");
        return score;
    }

    /**
     * Smart resume / skill match score: simple but very effective.
     */
    public JobDtos.MatchScoreResponse computeMatch(List<String> studentSkills, List<String> requiredSkills) {
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return JobDtos.MatchScoreResponse.builder()
                    .matchScore(100).matchedSkills(matched).missingSkills(missing).build();
        }
        Set<String> studentSet = (studentSkills == null ? new HashSet<String>()
                : studentSkills.stream().map(String::toLowerCase).collect(Collectors.toSet()));
        for (String req : requiredSkills) {
            if (studentSet.contains(req.toLowerCase())) matched.add(req);
            else missing.add(req);
        }
        int score = (int) Math.round(100.0 * matched.size() / requiredSkills.size());
        return JobDtos.MatchScoreResponse.builder()
                .matchScore(score).matchedSkills(matched).missingSkills(missing).build();
    }

    public JobDtos.MatchScoreResponse matchForStudent(Long studentId, Long jobId) {
        Student s = studentRepo.findById(studentId).orElseThrow(() -> ApiException.notFound("Student not found"));
        s.setSkills(studentRepo.findSkills(studentId));
        Job j = getById(jobId);
        return isEligible(s, j);
    }

    private JobDtos.MatchScoreResponse failed(String reason) {
        return JobDtos.MatchScoreResponse.builder()
                .matchScore(0).matchedSkills(List.of()).missingSkills(List.of())
                .eligible(false).eligibilityReason(reason).build();
    }
}

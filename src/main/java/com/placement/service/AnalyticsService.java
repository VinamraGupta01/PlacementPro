package com.placement.service;

import com.placement.repository.ApplicationRepository;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final StudentRepository studentRepo;
    private final CompanyRepository companyRepo;
    private final JobRepository jobRepo;
    private final ApplicationRepository appRepo;

    public AnalyticsService(StudentRepository s, CompanyRepository c, JobRepository j, ApplicationRepository a) {
        this.studentRepo = s; this.companyRepo = c; this.jobRepo = j; this.appRepo = a;
    }

    public Map<String, Object> dashboard() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("totalStudents", studentRepo.count());
        map.put("totalCompanies", companyRepo.count());
        map.put("totalJobs", jobRepo.count());
        map.put("totalApplications", appRepo.count());
        map.put("countsByStatus", appRepo.countsByStatus());
        map.put("applicationsPerCompany", appRepo.applicationsPerCompany());
        map.put("selectionsByBranch", appRepo.selectionsByBranch());
        return map;
    }

    public List<Map<String, Object>> applicationsPerCompany() { return appRepo.applicationsPerCompany(); }
    public List<Map<String, Object>> selectionsByBranch() { return appRepo.selectionsByBranch(); }
}

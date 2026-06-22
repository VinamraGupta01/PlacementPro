package com.placement.config;

import com.placement.model.Admin;
import com.placement.model.Company;
import com.placement.model.Job;
import com.placement.model.Student;
import com.placement.repository.AdminRepository;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Seeds the database with a default admin + sample data so the app
 * looks great the first time you open it.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AdminRepository adminRepo;
    private final StudentRepository studentRepo;
    private final CompanyRepository companyRepo;
    private final JobRepository jobRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(AdminRepository a, StudentRepository s, CompanyRepository c,
                           JobRepository j, PasswordEncoder encoder) {
        this.adminRepo = a;
        this.studentRepo = s;
        this.companyRepo = c;
        this.jobRepo = j;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedStudents();
        seedCompaniesAndJobs();
    }

    private void seedAdmin() {
        if (!adminRepo.existsByEmail("admin@college.edu")) {
            Admin a = Admin.builder()
                    .name("Placement Officer")
                    .email("admin@college.edu")
                    .password(encoder.encode("admin123"))
                    .role("ADMIN")
                    .build();
            adminRepo.save(a);
            log.info("Seeded default admin -> admin@college.edu / admin123");
        }
    }

    private void seedStudents() {
        if (studentRepo.count() > 0) return;

        Long s1 = studentRepo.save(Student.builder()
                .name("Aarav Sharma").email("aarav@student.edu").password(encoder.encode("student123"))
                .branch("CSE").cgpa(8.6).passingYear(2026).phone("9000000001").role("STUDENT").build());
        List.of("Java", "Spring Boot", "MySQL", "REST API", "Git").forEach(sk -> studentRepo.addSkill(s1, sk));

        Long s2 = studentRepo.save(Student.builder()
                .name("Priya Verma").email("priya@student.edu").password(encoder.encode("student123"))
                .branch("IT").cgpa(7.9).passingYear(2026).phone("9000000002").role("STUDENT").build());
        List.of("Java", "MySQL", "HTML", "CSS").forEach(sk -> studentRepo.addSkill(s2, sk));

        Long s3 = studentRepo.save(Student.builder()
                .name("Rohan Mehta").email("rohan@student.edu").password(encoder.encode("student123"))
                .branch("ECE").cgpa(6.8).passingYear(2026).phone("9000000003").role("STUDENT").build());
        List.of("C", "C++", "VLSI").forEach(sk -> studentRepo.addSkill(s3, sk));

        log.info("Seeded 3 demo students (password: student123)");
    }

    private void seedCompaniesAndJobs() {
        if (companyRepo.count() > 0) return;

        Long c1 = companyRepo.save(Company.builder()
                .name("InnoTech Solutions").website("https://innotech.example.com")
                .location("Bengaluru").description("Product engineering & cloud services.").build());

        Long c2 = companyRepo.save(Company.builder()
                .name("FinEdge Capital").website("https://finedge.example.com")
                .location("Mumbai").description("FinTech platform for retail investors.").build());

        Long c3 = companyRepo.save(Company.builder()
                .name("DataNova Analytics").website("https://datanova.example.com")
                .location("Hyderabad").description("Big data, ML and BI consulting.").build());

        Long j1 = jobRepo.save(Job.builder()
                .companyId(c1).title("Backend Engineer Intern")
                .description("Build REST APIs using Java, Spring Boot and MySQL. Work with senior engineers on production systems.")
                .minCgpa(7.5).eligibleBranches("CSE,IT").passingYear(2026)
                .jobType("INTERNSHIP").location("Bengaluru / Remote")
                .deadline(LocalDate.now().plusDays(30)).build());
        List.of("Java", "Spring Boot", "MySQL", "REST API").forEach(sk -> jobRepo.addRequiredSkill(j1, sk));

        Long j2 = jobRepo.save(Job.builder()
                .companyId(c2).title("Full Stack Developer")
                .description("Develop FinTech dashboards using React + Spring Boot. CGPA 7+ required.")
                .minCgpa(7.0).eligibleBranches("CSE,IT,ECE").passingYear(2026)
                .jobType("FULL_TIME").location("Mumbai")
                .deadline(LocalDate.now().plusDays(45)).build());
        List.of("Java", "Spring Boot", "React", "JavaScript", "MySQL").forEach(sk -> jobRepo.addRequiredSkill(j2, sk));

        Long j3 = jobRepo.save(Job.builder()
                .companyId(c3).title("Data Analyst Intern")
                .description("Work on SQL pipelines, dashboards and reporting for client engagements.")
                .minCgpa(6.5).eligibleBranches("CSE,IT,ECE,EEE").passingYear(2026)
                .jobType("INTERNSHIP").location("Hyderabad")
                .deadline(LocalDate.now().plusDays(20)).build());
        List.of("SQL", "Python", "Excel", "Power BI").forEach(sk -> jobRepo.addRequiredSkill(j3, sk));

        log.info("Seeded 3 companies and 3 jobs.");
    }
}

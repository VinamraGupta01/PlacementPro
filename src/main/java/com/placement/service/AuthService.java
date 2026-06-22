package com.placement.service;

import com.placement.dto.AuthDtos;
import com.placement.exception.ApiException;
import com.placement.model.Admin;
import com.placement.model.Student;
import com.placement.repository.AdminRepository;
import com.placement.repository.StudentRepository;
import com.placement.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final StudentRepository studentRepo;
    private final AdminRepository adminRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(StudentRepository s, AdminRepository a, PasswordEncoder e, JwtUtil j) {
        this.studentRepo = s; this.adminRepo = a; this.encoder = e; this.jwt = j;
    }

    public AuthDtos.AuthResponse registerStudent(AuthDtos.StudentRegisterRequest req) {
        if (studentRepo.existsByEmail(req.getEmail()) || adminRepo.existsByEmail(req.getEmail())) {
            throw ApiException.conflict("Email already registered");
        }
        Student s = Student.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .branch(req.getBranch())
                .cgpa(req.getCgpa() == null ? 0.0 : req.getCgpa())
                .passingYear(req.getPassingYear())
                .phone(req.getPhone())
                .role("STUDENT")
                .build();
        Long id = studentRepo.save(s);
        String token = jwt.generate(s.getEmail(), "STUDENT", id, s.getName());
        return AuthDtos.AuthResponse.builder()
                .token(token).role("STUDENT").userId(id).name(s.getName()).email(s.getEmail()).build();
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
        // Admin first
        var admin = adminRepo.findByEmail(req.getEmail());
        if (admin.isPresent()) {
            Admin a = admin.get();
            if (!encoder.matches(req.getPassword(), a.getPassword())) {
                throw ApiException.unauthorized("Invalid credentials");
            }
            String token = jwt.generate(a.getEmail(), "ADMIN", a.getId(), a.getName());
            return AuthDtos.AuthResponse.builder()
                    .token(token).role("ADMIN").userId(a.getId()).name(a.getName()).email(a.getEmail()).build();
        }
        // Student
        var student = studentRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> ApiException.unauthorized("Invalid credentials"));
        if (!encoder.matches(req.getPassword(), student.getPassword())) {
            throw ApiException.unauthorized("Invalid credentials");
        }
        String token = jwt.generate(student.getEmail(), "STUDENT", student.getId(), student.getName());
        return AuthDtos.AuthResponse.builder()
                .token(token).role("STUDENT").userId(student.getId()).name(student.getName()).email(student.getEmail()).build();
    }
}

package com.placement.service;

import com.placement.exception.ApiException;
import com.placement.model.Student;
import com.placement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository repo;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    public Student getById(Long id) {
        Student s = repo.findById(id).orElseThrow(() -> ApiException.notFound("Student not found"));
        s.setSkills(repo.findSkills(id));
        s.setPassword(null);
        return s;
    }

    public List<Student> getAll() {
        List<Student> all = repo.findAll();
        all.forEach(s -> { s.setSkills(repo.findSkills(s.getId())); s.setPassword(null); });
        return all;
    }

    public Student update(Long id, Student updates) {
        Student existing = repo.findById(id).orElseThrow(() -> ApiException.notFound("Student not found"));
        existing.setName(updates.getName() != null ? updates.getName() : existing.getName());
        existing.setBranch(updates.getBranch() != null ? updates.getBranch() : existing.getBranch());
        existing.setCgpa(updates.getCgpa() != null ? updates.getCgpa() : existing.getCgpa());
        existing.setPassingYear(updates.getPassingYear() != null ? updates.getPassingYear() : existing.getPassingYear());
        existing.setPhone(updates.getPhone() != null ? updates.getPhone() : existing.getPhone());
        repo.update(existing);
        return getById(id);
    }

    public List<String> setSkills(Long id, List<String> skills) {
        repo.findById(id).orElseThrow(() -> ApiException.notFound("Student not found"));
        repo.clearSkills(id);
        if (skills != null) {
            skills.stream()
                    .filter(s -> s != null && !s.isBlank())
                    .map(String::trim)
                    .distinct()
                    .forEach(s -> repo.addSkill(id, s));
        }
        return repo.findSkills(id);
    }

    public String uploadResume(Long studentId, MultipartFile file) {
        if (file == null || file.isEmpty()) throw ApiException.badRequest("Empty file");
        String original = file.getOriginalFilename() == null ? "resume.pdf" : file.getOriginalFilename();
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
        String fileName = "student_" + studentId + "_" + UUID.randomUUID() + ext;
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            Path target = dir.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            String url = "/uploads/" + fileName;
            repo.updateResume(studentId, url);
            return url;
        } catch (IOException e) {
            throw ApiException.badRequest("Failed to store file: " + e.getMessage());
        }
    }
}

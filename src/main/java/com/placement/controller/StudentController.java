package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.model.Student;
import com.placement.service.ApplicationService;
import com.placement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student profile, skills, resume & applications")
public class StudentController {

    private final StudentService studentService;
    private final ApplicationService applicationService;

    public StudentController(StudentService s, ApplicationService a) {
        this.studentService = s; this.applicationService = a;
    }

    @GetMapping
    @Operation(summary = "List all students")
    public ApiResponse<List<Student>> listAll() {
        return ApiResponse.ok(studentService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student profile")
    public ApiResponse<Student> get(@PathVariable Long id) {
        return ApiResponse.ok(studentService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student profile")
    public ApiResponse<Student> update(@PathVariable Long id, @RequestBody Student updates) {
        return ApiResponse.ok("Profile updated", studentService.update(id, updates));
    }

    @PostMapping("/{id}/skills")
    @Operation(summary = "Replace student skills")
    public ApiResponse<List<String>> setSkills(@PathVariable Long id, @RequestBody Map<String, List<String>> body) {
        return ApiResponse.ok("Skills updated", studentService.setSkills(id, body.get("skills")));
    }

    @PostMapping(value = "/{id}/resume", consumes = "multipart/form-data")
    @Operation(summary = "Upload student resume (PDF/DOCX)")
    public ApiResponse<Map<String, String>> uploadResume(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) {
        String url = studentService.uploadResume(id, file);
        return ApiResponse.ok("Resume uploaded", Map.of("resumeUrl", url));
    }

    @GetMapping("/{id}/applications")
    @Operation(summary = "Get applications for a student")
    public ApiResponse<?> myApplications(@PathVariable Long id) {
        return ApiResponse.ok(applicationService.findByStudent(id));
    }
}

package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.model.Application;
import com.placement.model.Company;
import com.placement.service.AnalyticsService;
import com.placement.service.ApplicationService;
import com.placement.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin-only endpoints (companies, applications, dashboard, exports)")
public class AdminController {

    private final CompanyService companyService;
    private final ApplicationService applicationService;
    private final AnalyticsService analyticsService;

    public AdminController(CompanyService c, ApplicationService a, AnalyticsService an) {
        this.companyService = c; this.applicationService = a; this.analyticsService = an;
    }

    // ---------- companies ----------
    @PostMapping("/companies")
    @Operation(summary = "Add a new company")
    public ApiResponse<Company> createCompany(@RequestBody Company c) {
        return ApiResponse.ok("Company added", companyService.create(c));
    }

    @GetMapping("/companies")
    @Operation(summary = "List all companies")
    public ApiResponse<List<Company>> companies() {
        return ApiResponse.ok(companyService.getAll());
    }

    @DeleteMapping("/companies/{id}")
    @Operation(summary = "Delete a company")
    public ApiResponse<Void> deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
        return ApiResponse.ok("Company deleted", null);
    }

    // ---------- applications ----------
    @GetMapping("/applications")
    @Operation(summary = "List all applications")
    public ApiResponse<List<Application>> apps() { return ApiResponse.ok(applicationService.findAll()); }

    @PutMapping("/applications/{id}/status")
    @Operation(summary = "Update application status (APPLIED, SHORTLISTED, INTERVIEW, SELECTED, REJECTED)")
    public ApiResponse<Application> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ApiResponse.ok("Status updated", applicationService.updateStatus(id, body.get("status")));
    }

    @PutMapping("/applications/{id}/notes")
    @Operation(summary = "Add/update notes for an application")
    public ApiResponse<Application> updateNotes(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ApiResponse.ok("Notes updated", applicationService.updateNotes(id, body.get("notes")));
    }

    // ---------- dashboard ----------
    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard analytics")
    public ApiResponse<Map<String, Object>> dashboard() { return ApiResponse.ok(analyticsService.dashboard()); }

    // ---------- export CSV ----------
    @GetMapping(value = "/export/applications", produces = "text/csv")
    @Operation(summary = "Export all applications as CSV")
    public void exportApplications(HttpServletResponse res) throws IOException {
        res.setContentType(MediaType.parseMediaType("text/csv").toString());
        res.setHeader("Content-Disposition", "attachment; filename=applications.csv");
        try (PrintWriter w = res.getWriter()) {
            w.println("ID,Student,Email,Job,Company,Status,Match %,Applied At");
            for (Application a : applicationService.findAll()) {
                w.printf("%d,\"%s\",%s,\"%s\",\"%s\",%s,%d,%s%n",
                        a.getId(),
                        nz(a.getStudentName()), nz(a.getStudentEmail()),
                        nz(a.getJobTitle()), nz(a.getCompanyName()),
                        nz(a.getStatus()),
                        a.getMatchScore() == null ? 0 : a.getMatchScore(),
                        a.getAppliedAt() == null ? "" : a.getAppliedAt().toString());
            }
        }
    }

    private static String nz(String s) { return s == null ? "" : s.replace("\"", "'"); }
}

package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.AuthDtos;
import com.placement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Registration & login endpoints")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    @Operation(summary = "Register a new student account")
    public ApiResponse<AuthDtos.AuthResponse> register(@Valid @RequestBody AuthDtos.StudentRegisterRequest req) {
        return ApiResponse.ok("Registered successfully", auth.registerStudent(req));
    }

    @PostMapping("/login")
    @Operation(summary = "Login as student or admin")
    public ApiResponse<AuthDtos.AuthResponse> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        return ApiResponse.ok("Login successful", auth.login(req));
    }
}

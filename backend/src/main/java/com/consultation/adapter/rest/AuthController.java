package com.consultation.adapter.rest;

import com.consultation.application.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r) {
        return ResponseEntity.ok(authService.register(r.email, r.password, r.name, r.phone));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest r) {
        return ResponseEntity.ok(authService.login(r.email, r.password));
    }

    @Data
    public static class RegisterRequest {
        @NotBlank @Email String email;
        @NotBlank @Size(min = 8) String password;
        @NotBlank String name;
        String phone;
    }

    @Data
    public static class LoginRequest {
        @NotBlank @Email String email;
        @NotBlank String password;
    }
}

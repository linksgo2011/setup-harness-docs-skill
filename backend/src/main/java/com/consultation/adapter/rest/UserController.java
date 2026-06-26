package com.consultation.adapter.rest;

import com.consultation.application.service.UserService;
import com.consultation.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> profile() { return ResponseEntity.ok(userService.getProfile(SecurityUtils.currentUserId())); }

    @PutMapping("/me")
    public ResponseEntity<?> update(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updateProfile(SecurityUtils.currentUserId(), body.get("name"), body.get("phone")));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePw(@RequestBody Map<String, String> body) {
        userService.changePassword(SecurityUtils.currentUserId(), body.get("oldPassword"), body.get("newPassword"));
        return ResponseEntity.ok().build();
    }
}

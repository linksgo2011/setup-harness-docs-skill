package com.consultation.adapter.rest;

import com.consultation.application.service.ConsultantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/consultants")
@RequiredArgsConstructor
public class ConsultantController {

    private final ConsultantService consultantService;

    @GetMapping
    public ResponseEntity<?> list() { return ResponseEntity.ok(consultantService.findAllActive()); }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) { return ResponseEntity.ok(consultantService.getById(id)); }

    @GetMapping("/{id}/slots")
    public ResponseEntity<List<Map<String, String>>> slots(@PathVariable String id, @RequestParam String date) {
        return ResponseEntity.ok(consultantService.getAvailableSlots(id, date));
    }
}

package com.servify.admin.controller;

import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.AdminDashboardStats;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.admin.dto.UpdateProviderStatusRequest;
import com.servify.admin.service.AdminService;
import com.servify.provider.model.ProviderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AdminResponse>> findAllAdmins() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> findAdminById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable("id") Long id, @Valid @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardStats> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/providers")
    public ResponseEntity<List<ProviderApplicationResponse>> findProviderApplications(
        @RequestParam(value = "status", required = false) ProviderStatus status
    ) {
        return ResponseEntity.ok(adminService.findProviderApplications(status));
    }

    @PatchMapping("/providers/{providerId}/status")
    public ResponseEntity<ProviderApplicationResponse> updateProviderStatus(
        @PathVariable("providerId") Long providerId,
        @Valid @RequestBody UpdateProviderStatusRequest request
    ) {
        return ResponseEntity.ok(adminService.updateProviderStatus(providerId, request.getStatus()));
    }
}

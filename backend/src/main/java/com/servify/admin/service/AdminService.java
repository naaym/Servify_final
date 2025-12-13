package com.servify.admin.service;

import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.provider.model.ProviderStatus;

import java.util.List;

public interface AdminService {
    AdminResponse create(AdminRequest request);

    List<AdminResponse> findAll();

    AdminResponse findById(Long id);

    AdminResponse update(Long id, AdminRequest request);

    void delete(Long id);

    List<ProviderApplicationResponse> findProviderApplications(ProviderStatus status);

    ProviderApplicationResponse updateProviderStatus(Long providerId, ProviderStatus status);
}

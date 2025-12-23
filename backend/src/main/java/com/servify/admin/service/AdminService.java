package com.servify.admin.service;

import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.AdminDashboardStats;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.client.dto.ClientResponse;
import com.servify.provider.model.ProviderStatus;

import java.util.List;

public interface AdminService {
    AdminResponse create(AdminRequest request);

    List<AdminResponse> findAll();

    AdminResponse findById(Long id);

    AdminResponse update(Long id, AdminRequest request);

    void delete(Long id);

    AdminDashboardStats getDashboardStats();

    List<ProviderApplicationResponse> findProviderApplications(ProviderStatus status);

    ProviderApplicationResponse updateProviderStatus(Long providerId, ProviderStatus status);

    void deleteProvider(Long providerId);

    List<ClientResponse> findAllClients();

    void deleteClient(Long clientId);
}

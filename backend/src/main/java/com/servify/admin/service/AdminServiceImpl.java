package com.servify.admin.service;

import com.servify.admin.dto.AdminDashboardStats;
import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.admin.mapper.AdminMapper;
import com.servify.admin.model.AdminEntity;
import com.servify.admin.repository.AdminRepository;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.servify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import com.servify.user.enums.Role;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ProviderRepository providerRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final AdminMapper adminMapper;

    @Override
    public AdminResponse create(AdminRequest request) {
        ensureEmailIsAvailable(request.getEmail());
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required for admin creation");
        }
        AdminEntity saved = adminRepository.save(adminMapper.toEntity(request));
        return adminMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminResponse> findAll() {
        return adminRepository.findAll().stream()
            .map(adminMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponse findById(Long id) {
        AdminEntity admin = adminRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + id));
        return adminMapper.toResponse(admin);
    }

    @Override
    public AdminResponse update(Long id, AdminRequest request) {
        AdminEntity admin = adminRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + id));
        if (!admin.getEmail().equals(request.getEmail())) {
            ensureEmailIsAvailable(request.getEmail());
            admin.setEmail(request.getEmail());
        }
        adminMapper.updateEntity(admin, request);
        return adminMapper.toResponse(admin);
    }

    @Override
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found: " + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardStats getDashboardStats() {
        AdminDashboardStats stats = new AdminDashboardStats();
        long providerCount = providerRepository.count();
        long clientCount = clientRepository.count();
        long adminCount = adminRepository.count();

        stats.setProviders(providerCount);
        stats.setClients(clientCount);
        stats.setServices(0L);
        stats.setPendingProviders(providerRepository.countByStatus(ProviderStatus.PENDING));
        stats.setAcceptedProviders(providerRepository.countByStatus(ProviderStatus.ACCEPTED));
        stats.setRejectedProviders(providerRepository.countByStatus(ProviderStatus.REJECTED));

        Role currentRole = resolveCurrentUserRole();
        if (currentRole == Role.SUPER_ADMIN) {
            stats.setUsers(providerCount + clientCount + adminCount);
        } else if (currentRole == Role.ADMIN) {
            stats.setUsers(providerCount + clientCount);
        } else {
            stats.setUsers(userRepository.count());
        }
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderApplicationResponse> findProviderApplications(ProviderStatus status) {
        List<ProviderEntity> providers = Optional.ofNullable(status)
            .map(providerRepository::findAllByStatus)
            .orElseGet(providerRepository::findAll);

        return providers.stream()
            .map(adminMapper::toProviderApplication)
            .toList();
    }

    @Override
    public ProviderApplicationResponse updateProviderStatus(Long providerId, ProviderStatus status) {
        ProviderEntity provider = providerRepository.findById(providerId)
            .orElseThrow(() -> new ResourceNotFoundException("Provider not found: " + providerId));
        provider.setStatus(status);
        return adminMapper.toProviderApplication(provider);
    }

    private void ensureEmailIsAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already used: " + email);
        }
    }

    private Role resolveCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String name = authority.getAuthority();
            if (name != null && name.startsWith("ROLE_")) {
                try {
                    return Role.valueOf(name.substring("ROLE_".length()));
                } catch (IllegalArgumentException ignored) {
                    // continue
                }
            }
        }
        return null;
    }
}

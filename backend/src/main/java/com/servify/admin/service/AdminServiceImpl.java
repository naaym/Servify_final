package com.servify.admin.service;

import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.admin.mapper.AdminMapper;
import com.servify.admin.model.AdminEntity;
import com.servify.admin.repository.AdminRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.servify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ProviderRepository providerRepository;
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
    public List<AdminResponse> findAll() {
        return adminRepository.findAll().stream()
            .map(adminMapper::toResponse)
            .toList();
    }

    @Override
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
}

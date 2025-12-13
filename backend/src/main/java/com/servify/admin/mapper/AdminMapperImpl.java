package com.servify.admin.mapper;

import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.admin.model.AdminEntity;
import com.servify.provider.model.ProviderEntity;
import com.servify.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminMapperImpl implements AdminMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminEntity toEntity(AdminRequest request) {
        AdminEntity entity = new AdminEntity();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setPhone(request.getPhone());
        entity.setGovernorate(request.getGovernorate());
        entity.setRole(Role.ADMIN);
        return entity;
    }

    @Override
    public void updateEntity(AdminEntity entity, AdminRequest request) {
        entity.setName(request.getName());
        entity.setPhone(request.getPhone());
        entity.setGovernorate(request.getGovernorate());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    @Override
    public AdminResponse toResponse(AdminEntity entity) {
        AdminResponse response = new AdminResponse();
        response.setId(entity.getUserId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setPhone(entity.getPhone());
        response.setGovernorate(entity.getGovernorate());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    @Override
    public ProviderApplicationResponse toProviderApplication(ProviderEntity providerEntity) {
        ProviderApplicationResponse response = new ProviderApplicationResponse();
        response.setId(providerEntity.getUserId());
        response.setName(providerEntity.getName());
        response.setEmail(providerEntity.getEmail());
        response.setPhone(providerEntity.getPhone());
        response.setGovernorate(providerEntity.getGovernorate());
        response.setDelegation(providerEntity.getDelegation());
        response.setAge(providerEntity.getAge());
        response.setStatus(providerEntity.getStatus());
        response.setCin(providerEntity.getCin());
        response.setCv(providerEntity.getCv());
        response.setDiplome(providerEntity.getDiplome());
        response.setCreatedAt(providerEntity.getCreatedAt());
        return response;
    }
}

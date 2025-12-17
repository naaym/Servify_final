package com.servify.client.mapper;

import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.model.ClientEntity;
import com.servify.client.dto.ClientResponse;
import com.servify.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public  class ClientMapperImpl implements ClientMapper {
  private final PasswordEncoder passwordEncoder;


@Override
    public  ClientEntity toEntity(ClientSignUpRequest request) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName(request.getName());
        clientEntity.setEmail(request.getEmail());
        clientEntity.setPhone(request.getPhone());
        clientEntity.setGovernorate(request.getGovernorate());
        clientEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        clientEntity.setRole(Role.CLIENT);
        return clientEntity;
    }

    @Override
    public ClientResponse toResponse(ClientEntity entity) {
        ClientResponse response = new ClientResponse();
        response.setId(entity.getUserId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setPhone(entity.getPhone());
        response.setGovernorate(entity.getGovernorate());
        response.setProfileImageUrl(entity.getProfileImageUrl());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }


}

package com.servify.client.mapper;

import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.model.ClientEntity;
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


}

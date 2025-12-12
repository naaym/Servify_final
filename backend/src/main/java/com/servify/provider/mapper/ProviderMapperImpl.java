package com.servify.provider.mapper;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public  class ProviderMapperImpl implements ProviderMapper {
  private final PasswordEncoder passwordEncoder ;

  @Override
    public  ProviderEntity toEntity(ProviderRegistrationRequest request) {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setName(request.getName());
        providerEntity.setEmail(request.getEmail());
        providerEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        providerEntity.setPhone(request.getPhone());
        providerEntity.setGovernorate(request.getGovernorate());
        providerEntity.setDelegation(request.getDelegation());
        providerEntity.setAge(request.getAge());
        providerEntity.setStatus(ProviderStatus.PENDING);
        providerEntity.setCin(request.getCin());
        providerEntity.setCv(request.getCv());
        providerEntity.setDiplome(request.getDiplome());
      providerEntity.setRole(Role.PROVIDER);

        return providerEntity;
    }

}

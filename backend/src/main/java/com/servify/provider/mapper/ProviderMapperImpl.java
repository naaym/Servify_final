package com.servify.provider.mapper;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        providerEntity.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        providerEntity.setPhone(request.getPhone());
        providerEntity.setGovernorate(request.getGovernorate());
        providerEntity.setDelegation(request.getDelegation());
        providerEntity.setAge(request.getAge());
        providerEntity.setStatus(ProviderStatus.PENDING);
        providerEntity.setCin(request.getCin());
        providerEntity.setCv(request.getCv());
        providerEntity.setDiplome(request.getDiplome());
        return providerEntity;
    }

}

package com.servify.provider.service;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;

import com.servify.provider.exceptions.EmailDuplicationException;
import com.servify.provider.mapper.ProviderMapper;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService{

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;


    public ProviderRegistrationResponse register(ProviderRegistrationRequest request) {
        ensureEmailIsAvailable(request.getEmail());
        ProviderEntity savedEntity = providerMapper.toEntity(request);

        ProviderEntity saved = providerRepository.save(savedEntity);
        return new ProviderRegistrationResponse(saved.getUserId(), saved.getStatus());
    }

    private void ensureEmailIsAvailable(String email) {
        if(providerRepository.existsByEmail(email)){
          throw new EmailDuplicationException("Email "+email+" exists" );

    }}


}

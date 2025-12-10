package com.servify.provider.service;

import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.mapper.ProviderMapper;
import com.servify.provider.model.Provider;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProviderService {

    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public List<ProviderResponse> findAll() {
        return providerRepository.findAll()
                .stream()
                .map(ProviderMapper::toResponse)
                .toList();
    }

    public ProviderResponse findById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider %d not found".formatted(id)));
        return ProviderMapper.toResponse(provider);
    }

    public ProviderResponse create(ProviderRequest request) {
        Provider provider = ProviderMapper.toEntity(request);
        Provider saved = providerRepository.save(provider);
        return ProviderMapper.toResponse(saved);
    }

    public ProviderResponse update(Long id, ProviderRequest request) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider %d not found".formatted(id)));

        ProviderMapper.updateEntity(request, provider);
        Provider saved = providerRepository.save(provider);
        return ProviderMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Provider %d not found".formatted(id));
        }
        providerRepository.deleteById(id);
    }
}

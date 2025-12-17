package com.servify.provider.service;

import com.servify.provider.model.GovernorateEntity;
import com.servify.provider.model.ServiceCategoryEntity;
import com.servify.provider.repository.GovernorateRepository;
import com.servify.provider.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SearchOptionsService {

    private final ServiceCategoryRepository serviceCategoryRepository;
    private final GovernorateRepository governorateRepository;

    @Transactional(readOnly = true)
    public List<String> getAvailableServiceCategories() {
        return serviceCategoryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(ServiceCategoryEntity::getName)
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableGovernorates(String serviceCategory) {
        String normalizedService = normalizeValue(serviceCategory);
        if (normalizedService == null) {
            return List.of();
        }

        return serviceCategoryRepository.findByNameIgnoreCase(normalizedService)
                .map(service -> governorateRepository.findByServiceOrderByNameAsc(service)
                        .stream()
                        .map(GovernorateEntity::getName)
                        .filter(Objects::nonNull)
                        .toList())
                .orElseGet(List::of);
    }

    @Transactional
    public void registerServiceAndGovernorate(String serviceCategory, String governorate) {
        String normalizedService = normalizeValue(serviceCategory);
        String normalizedGovernorate = normalizeValue(governorate);

        if (normalizedService == null || normalizedGovernorate == null) {
            return;
        }

        ServiceCategoryEntity service = serviceCategoryRepository.findByNameIgnoreCase(normalizedService)
                .orElseGet(() -> {
                    ServiceCategoryEntity newService = new ServiceCategoryEntity();
                    newService.setName(normalizedService);
                    return serviceCategoryRepository.save(newService);
                });

        if (!governorateRepository.existsByServiceAndNameIgnoreCase(service, normalizedGovernorate)) {
            GovernorateEntity governorateEntity = new GovernorateEntity();
            governorateEntity.setService(service);
            governorateEntity.setName(normalizedGovernorate);
            governorateRepository.save(governorateEntity);
        }
    }

    private String normalizeValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        return normalized.isEmpty() ? null : normalized;
    }
}

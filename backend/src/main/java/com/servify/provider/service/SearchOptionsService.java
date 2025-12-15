package com.servify.provider.service;

import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchOptionsService {

    private final ProviderRepository providerRepository;

    public List<String> getAvailableServiceCategories() {
        List<String> services = providerRepository.findDistinctServiceCategoriesByStatus(ProviderStatus.ACCEPTED);
        return normalizeAndSort(services);
    }

    public List<String> getAvailableGovernorates(String serviceCategory) {
        List<String> governorates = providerRepository.findDistinctGovernoratesByStatusAndServiceCategory(
                ProviderStatus.ACCEPTED,
                normalizeValue(serviceCategory)
        );
        return normalizeAndSort(governorates);
    }

    private List<String> normalizeAndSort(List<String> values) {
        return values.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeValue)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    private String normalizeValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        return normalized.isEmpty() ? null : normalized;
    }
}

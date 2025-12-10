package com.servify.provider.mapper;

import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.model.Provider;

public final class ProviderMapper {

    private ProviderMapper() {
    }

    public static Provider toEntity(ProviderRequest request) {
        Provider provider = new Provider();
        provider.setCompanyName(request.getCompanyName());
        provider.setServiceCategory(request.getServiceCategory());
        provider.setHourlyRate(request.getHourlyRate());
        provider.setActive(request.isActive());
        return provider;
    }

    public static void updateEntity(ProviderRequest request, Provider provider) {
        provider.setCompanyName(request.getCompanyName());
        provider.setServiceCategory(request.getServiceCategory());
        provider.setHourlyRate(request.getHourlyRate());
        provider.setActive(request.isActive());
    }

    public static ProviderResponse toResponse(Provider provider) {
        ProviderResponse response = new ProviderResponse();
        response.setId(provider.getId());
        response.setCompanyName(provider.getCompanyName());
        response.setServiceCategory(provider.getServiceCategory());
        response.setHourlyRate(provider.getHourlyRate());
        response.setActive(provider.isActive());
        response.setCreatedAt(provider.getCreatedAt());
        return response;
    }
}

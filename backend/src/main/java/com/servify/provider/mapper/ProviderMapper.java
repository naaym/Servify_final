package com.servify.provider.mapper;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.dto.ProviderSearchResponse;
import com.servify.provider.model.ProviderEntity;

public interface ProviderMapper {
   ProviderEntity toEntity(ProviderRegistrationRequest request);

   ProviderSearchResponse toSearchResponse(ProviderEntity entity);




}

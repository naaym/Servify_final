package com.servify.provider.service;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderSearchRequest;
import com.servify.provider.dto.ProviderSearchResult;

public interface ProviderService {
  ProviderRegistrationResponse register(ProviderRegistrationRequest request);

  ProviderSearchResult searchProviders(ProviderSearchRequest request);
}

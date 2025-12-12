package com.servify.provider.service;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;

public interface ProviderService {
  ProviderRegistrationResponse register(ProviderRegistrationRequest request);
}

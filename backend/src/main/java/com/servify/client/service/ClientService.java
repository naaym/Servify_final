package com.servify.client.service;

import com.servify.client.dto.ClientSignUpResponse;
import com.servify.client.dto.ClientSignUpRequest;

public interface ClientService {
  ClientSignUpResponse register(ClientSignUpRequest request);
}

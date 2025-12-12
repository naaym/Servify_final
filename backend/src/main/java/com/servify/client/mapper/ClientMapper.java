package com.servify.client.mapper;

import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.model.ClientEntity;

public interface ClientMapper {
  ClientEntity toEntity(ClientSignUpRequest request);
}

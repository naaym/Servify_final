package com.servify.client.mapper;

import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.model.ClientEntity;
import com.servify.client.dto.ClientResponse;

public interface ClientMapper {
  ClientEntity toEntity(ClientSignUpRequest request);
  ClientResponse toResponse(ClientEntity entity);
}

package com.servify.client.service;

import com.servify.client.dto.ClientSignUpResponse;
import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.mapper.ClientMapper;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.exceptions.EmailDuplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;


  public ClientSignUpResponse register(ClientSignUpRequest request) {
    ensureEmailIsAvailable(request.getEmail());
    ClientEntity clientEntity = clientMapper.toEntity(request);
    ClientEntity saved = clientRepository.save(clientEntity);
    return new ClientSignUpResponse(saved.getUserId());
  }


  private void ensureEmailIsAvailable(String email) {
    if (clientRepository.existsByEmail(email)) {
      throw new EmailDuplicationException("Email " + email + " exists");
    }
  }
}

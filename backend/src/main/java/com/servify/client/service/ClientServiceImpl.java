package com.servify.client.service;

import com.servify.client.dto.ClientSignUpResponse;
import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.mapper.ClientMapper;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.client.dto.ClientResponse;
import com.servify.provider.exceptions.EmailDuplicationException;
import com.servify.provider.service.StorageFilesService;
import com.servify.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;
  private final StorageFilesService storageFilesService;


  public ClientSignUpResponse register(ClientSignUpRequest request) {
    ensureEmailIsAvailable(request.getEmail());
    ClientEntity clientEntity = clientMapper.toEntity(request);
    ClientEntity saved = clientRepository.save(clientEntity);
    return new ClientSignUpResponse(saved.getUserId());
  }

  @Override
  @Transactional(readOnly = true)
  public ClientResponse getCurrentProfile() {
    ClientEntity client = getCurrentClient();
    return clientMapper.toResponse(client);
  }

  @Override
  public ClientResponse updateProfilePhoto(MultipartFile photo) {
    ClientEntity client = getCurrentClient();
    String imageUrl = storageFilesService.storeImage(photo, "servify/clients/profile");
    client.setProfileImageUrl(imageUrl);
    ClientEntity saved = clientRepository.save(client);
    return clientMapper.toResponse(saved);
  }


  private void ensureEmailIsAvailable(String email) {
    if (clientRepository.existsByEmail(email)) {
      throw new EmailDuplicationException("Email " + email + " exists");
    }
  }

  private ClientEntity getCurrentClient() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return clientRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
  }
}

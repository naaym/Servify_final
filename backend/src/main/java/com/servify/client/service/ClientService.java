package com.servify.client.service;

import com.servify.client.dto.ClientSignUpResponse;
import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.dto.ClientResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ClientService {
  ClientSignUpResponse register(ClientSignUpRequest request);
  ClientResponse getCurrentProfile();
  ClientResponse updateProfilePhoto(MultipartFile photo);
}

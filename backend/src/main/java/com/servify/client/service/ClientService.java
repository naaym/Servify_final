package com.servify.client.service;

import com.servify.client.dto.ClientRequest;
import com.servify.client.dto.ClientResponse;
import com.servify.client.mapper.ClientMapper;
import com.servify.client.model.Client;
import com.servify.client.repository.ClientRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::toResponse)
                .toList();
    }

    public ClientResponse findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client %d not found".formatted(id)));
        return ClientMapper.toResponse(client);
    }

    public ClientResponse create(ClientRequest request) {
        clientRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email already in use: " + existing.getEmail());
                });

        Client client = ClientMapper.toEntity(request);
        Client saved = clientRepository.save(client);
        return ClientMapper.toResponse(saved);
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client %d not found".formatted(id)));

        clientRepository.findByEmail(request.getEmail())
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Email already in use: " + found.getEmail());
                });

        ClientMapper.updateEntity(request, client);
        Client saved = clientRepository.save(client);
        return ClientMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client %d not found".formatted(id));
        }
        clientRepository.deleteById(id);
    }
}

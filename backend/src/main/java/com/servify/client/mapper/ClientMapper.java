package com.servify.client.mapper;

import com.servify.client.dto.ClientRequest;
import com.servify.client.dto.ClientResponse;
import com.servify.client.model.Client;

public final class ClientMapper {

    private ClientMapper() {
    }

    public static Client toEntity(ClientRequest request) {
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        return client;
    }

    public static void updateEntity(ClientRequest request, Client client) {
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
    }

    public static ClientResponse toResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setCreatedAt(client.getCreatedAt());
        return response;
    }
}

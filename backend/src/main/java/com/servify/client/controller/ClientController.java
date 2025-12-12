package com.servify.client.controller;

import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.dto.ClientSignUpResponse;
import com.servify.client.service.ClientService;
import com.servify.client.service.ClientServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientSignUpResponse> register(@Valid @RequestBody ClientSignUpRequest request) {
        ClientSignUpResponse response = clientService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

package com.servify.client.controller;

import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.dto.ClientSignUpResponse;
import com.servify.client.dto.ClientResponse;
import com.servify.client.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/profile")
    public ResponseEntity<ClientResponse> getProfile() {
        return ResponseEntity.ok(clientService.getCurrentProfile());
    }

    @PostMapping(value = "/profile/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientResponse> updateProfilePhoto(@RequestPart("photo") MultipartFile photo) {
        return ResponseEntity.ok(clientService.updateProfilePhoto(photo));
    }

    @PostMapping("/register")
    public ResponseEntity<ClientSignUpResponse> register(@Valid @RequestBody ClientSignUpRequest request) {
        ClientSignUpResponse response = clientService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

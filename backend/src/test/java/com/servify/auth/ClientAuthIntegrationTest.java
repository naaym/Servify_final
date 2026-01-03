package com.servify.auth;

import com.servify.auth.dto.LoginRequest;
import com.servify.auth.dto.LoginResponse;
import com.servify.auth.service.AuthService;
import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.dto.ClientSignUpResponse;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.client.service.ClientService;
import com.servify.provider.exceptions.EmailDuplicationException;
import com.servify.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ClientAuthIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registersClientAndPersistsEncodedPassword() {
        ClientSignUpRequest request = buildRequest("Alice", "alice@test.com", "StrongPass!1");

        ClientSignUpResponse response = clientService.register(request);

        ClientEntity persisted = clientRepository.findById(response.getClientId()).orElseThrow();
        assertThat(persisted.getEmail()).isEqualTo("alice@test.com");
        assertThat(persisted.getRole()).isEqualTo(Role.CLIENT);
        assertThat(persisted.getPassword()).isNotEqualTo(request.getPassword());
        assertThat(passwordEncoder.matches("StrongPass!1", persisted.getPassword())).isTrue();
        assertThat(persisted.getCreatedAt()).isNotNull();
    }

    @Test
    void preventsDuplicateEmailRegistration() {
        ClientSignUpRequest first = buildRequest("Bob", "duplicate@test.com", "Password123");
        clientService.register(first);

        ClientSignUpRequest duplicate = buildRequest("Bobby", "duplicate@test.com", "Password123");

        assertThatThrownBy(() -> clientService.register(duplicate))
                .isInstanceOf(EmailDuplicationException.class);
        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    void logsInRegisteredClientAndReturnsJwt() {
        ClientSignUpRequest request = buildRequest("Carla", "carla@test.com", "Secret123!");
        ClientSignUpResponse registration = clientService.register(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("carla@test.com");
        loginRequest.setPassword("Secret123!");

        LoginResponse response = authService.login(loginRequest);

        assertThat(response.getId()).isEqualTo(registration.getClientId());
        assertThat(response.getRole()).isEqualTo("CLIENT");
        assertThat(response.getStatus()).isNull();
        assertThat(response.getAccessToken()).isNotBlank();
    }

    private ClientSignUpRequest buildRequest(String name, String email, String password) {
        ClientSignUpRequest request = new ClientSignUpRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone("12345678");
        request.setGovernorate("tunis");
        return request;
    }
}
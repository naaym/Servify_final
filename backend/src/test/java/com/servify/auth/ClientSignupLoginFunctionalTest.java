package com.servify.auth;

import com.servify.auth.dto.LoginRequest;
import com.servify.auth.dto.LoginResponse;
import com.servify.client.dto.ClientSignUpRequest;
import com.servify.client.dto.ClientSignUpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientSignupLoginFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void clientRegistersThenLogsInViaHttpEndpoints() {
        String uniqueEmail = "functional.test." + System.currentTimeMillis() + "@servify.test";
        String password = "StrongPass!42";

        ClientSignUpRequest signUpRequest = new ClientSignUpRequest();
        signUpRequest.setName("Functional Tester");
        signUpRequest.setEmail(uniqueEmail);
        signUpRequest.setPassword(password);
        signUpRequest.setPhone("12345678");
        signUpRequest.setGovernorate("tunis");

        ResponseEntity<ClientSignUpResponse> signUpResponse = restTemplate.postForEntity(
                "/api/clients/register",
                signUpRequest,
                ClientSignUpResponse.class
        );

        assertThat(signUpResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(signUpResponse.getBody()).isNotNull();
        Long clientId = signUpResponse.getBody().getClientId();
        assertThat(clientId).isNotNull();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(uniqueEmail);
        loginRequest.setPassword(password);

        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "/api/auth/login",
                loginRequest,
                LoginResponse.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        LoginResponse loginBody = loginResponse.getBody();
        assertThat(loginBody.getId()).isEqualTo(clientId);
        assertThat(loginBody.getRole()).isEqualTo("CLIENT");
        assertThat(loginBody.getStatus()).isNull();
        assertThat(loginBody.getAccessToken()).isNotBlank();
    }
}
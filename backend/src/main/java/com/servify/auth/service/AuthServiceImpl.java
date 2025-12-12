package com.servify.auth.service;

import com.servify.auth.dto.LoginRequest;
import com.servify.auth.dto.LoginResponse;
import com.servify.auth.model.AuthenticatedUser;
import com.servify.provider.model.ProviderStatus;
import com.servify.shared.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            return LoginResponse.builder()
                    .accessToken(token)
                    .id(user.getId())
                    .role(user.getRole().name())
                    .status(user.getProviderStatus())
                    .message(buildStatusMessage(user))
                    .build();
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    private String buildStatusMessage(AuthenticatedUser user) {
        if (user.getRole() != Role.PROVIDER) {
            return "Login successful";
        }

        ProviderStatus status = user.getProviderStatus();
        if (status == null) {
            return "Login successful";
        }

        return switch (status) {
            case ACCEPTED -> "Your provider account is approved";
            case PENDING -> "Your provider account is under review";
            case REJECTED -> "Your provider account has been rejected";
        };
    }
}

package com.servify.auth.service;

import com.servify.auth.dto.LoginRequest;
import com.servify.auth.dto.LoginResponse;
import com.servify.provider.model.ProviderEntity;
import com.servify.user.model.UserEntity;
import com.servify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
      authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      String providerStatus=null;
      UserEntity user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(()-> new RuntimeException("User not found"));

            String token = jwtService.generateToken(user);
            if (user instanceof ProviderEntity provider){
              providerStatus=provider.getStatus().name();
            }

            return LoginResponse.builder()
                    .accessToken(token)
                    .id(user.getUserId())
                    .role(user.getRole().name())
                    .status(providerStatus)
                    .build();

    }


}

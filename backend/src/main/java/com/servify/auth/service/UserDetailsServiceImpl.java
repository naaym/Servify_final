package com.servify.auth.service;

import com.servify.auth.model.AuthenticatedUser;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return providerRepository.findByEmail(username)
                .map(AuthenticatedUser::fromProvider)
                .or(() -> clientRepository.findByEmail(username).map(AuthenticatedUser::fromClient))
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + username));
    }
}

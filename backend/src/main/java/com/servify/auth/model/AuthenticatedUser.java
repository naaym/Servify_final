package com.servify.auth.model;

import com.servify.client.model.ClientEntity;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.shared.security.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
public class AuthenticatedUser implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
    private final ProviderStatus providerStatus;

    public static AuthenticatedUser fromClient(ClientEntity client) {
        return AuthenticatedUser.builder()
                .id(client.getId())
                .email(client.getEmail())
                .password(client.getPasswordHash())
                .role(Role.CLIENT)
                .build();
    }

    public static AuthenticatedUser fromProvider(ProviderEntity provider) {
        return AuthenticatedUser.builder()
                .id(provider.getId())
                .email(provider.getEmail())
                .password(provider.getPasswordHash())
                .role(Role.PROVIDER)
                .providerStatus(provider.getStatus())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

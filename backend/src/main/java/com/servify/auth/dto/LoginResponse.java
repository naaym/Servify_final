package com.servify.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.servify.provider.model.ProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private Long id;

    private String role;

    private ProviderStatus status;

    private String message;
}

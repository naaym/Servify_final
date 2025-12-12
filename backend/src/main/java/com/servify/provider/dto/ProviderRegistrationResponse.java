package com.servify.provider.dto;

import com.servify.provider.model.ProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProviderRegistrationResponse {

    private Long providerId;
    private ProviderStatus status;


}

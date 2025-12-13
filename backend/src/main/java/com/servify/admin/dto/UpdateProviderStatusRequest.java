package com.servify.admin.dto;

import com.servify.provider.model.ProviderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProviderStatusRequest {
    @NotNull
    private ProviderStatus status;
}

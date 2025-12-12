package com.servify.provider.dto;

import com.servify.provider.model.ProviderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
public class ProviderResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String governorate;
    private String delegation;
    private Integer age;
    private ProviderStatus status;
    private Instant createdAt;


}

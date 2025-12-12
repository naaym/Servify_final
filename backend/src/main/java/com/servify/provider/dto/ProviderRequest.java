package com.servify.provider.dto;

import com.servify.provider.model.ProviderStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotBlank
    @Size(max = 100)
    private String governorate;

    @NotBlank
    @Size(max = 100)
    private String delegation;

    @NotNull
    @Min(18)
    private Integer age;


}

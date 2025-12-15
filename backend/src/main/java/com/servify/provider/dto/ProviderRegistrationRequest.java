package com.servify.provider.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class ProviderRegistrationRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Governorate is required")
    private String governorate;

    @NotBlank(message = "Delegation is required")
    private String delegation;

    @Min(value = 18, message = "Age must be at least 18")
    @NotNull(message = "Age is required")
    private Integer age;

    @NotBlank(message = "Service category is required")
    private String serviceCategory;

    @Min(value = 0, message = "Base price must be positive")
    @NotNull(message = "Base price is required")
    private Double basePrice;

    private String description;

    @NotNull(message = "CIN document is required")
    private MultipartFile cin;

    @NotNull(message = "CV document is required")
    private MultipartFile cv;

    @NotNull(message = "Diploma document is required")
    private MultipartFile diplome;

    private String cinName;
    private String cvName;
    private String diplomeName;

}

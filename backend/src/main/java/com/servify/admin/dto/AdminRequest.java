package com.servify.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotBlank
    private String phone;

    @NotBlank
    private String governorate;
}

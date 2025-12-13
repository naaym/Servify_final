package com.servify.provider.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class ProviderRegistrationRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String governorate;
    private String delegation;
    private int age;
    private String  cin;
    private String cv;
    private String diplome;


}

package com.servify.provider.controller;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.service.ProviderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProviderRegistrationResponse> register(
      @RequestParam("name") String name,
      @RequestParam("email") String email,
      @RequestParam("password") String password,
      @RequestParam("phone") String phone,
      @RequestParam("governorate") String governorate,
      @RequestParam("delegation") String delegation,
      @RequestParam("age") Integer age,
      @RequestPart("cin") MultipartFile cin,
      @RequestPart("cv") MultipartFile cv,
      @RequestPart("diplome") MultipartFile diplome
    ) {

        ProviderRegistrationRequest request = new ProviderRegistrationRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone(phone);
        request.setGovernorate(governorate);
        request.setDelegation(delegation);
        request.setAge(age);
        request.setCin(cin.getOriginalFilename());
        request.setCv(cv.getOriginalFilename());
        request.setDiplome(diplome.getOriginalFilename());

        ProviderRegistrationResponse response = providerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

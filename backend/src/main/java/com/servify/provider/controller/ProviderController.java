package com.servify.provider.controller;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderSearchRequest;
import com.servify.provider.dto.ProviderSearchResult;
import com.servify.provider.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping("/search")
    public ResponseEntity<ProviderSearchResult> searchProviders(ProviderSearchRequest request) {
        return ResponseEntity.ok(providerService.searchProviders(request));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProviderRegistrationResponse> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("governorate") String governorate,
            @RequestParam("delegation") String delegation,
            @RequestParam("age") Integer age,
            @RequestParam("serviceCategory") String serviceCategory,
            @RequestParam("basePrice") Double basePrice,
            @RequestParam(value = "description", required = false) String description,
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
        request.setServiceCategory(serviceCategory);
        request.setBasePrice(basePrice);
        request.setDescription(description);
        request.setCin(cin);
        request.setCv(cv);
        request.setDiplome(diplome);

        ProviderRegistrationResponse response = providerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

package com.servify.provider.mapper;

import com.servify.provider.dto.ProviderProfileResponse;
import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderSearchResponse;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public  class ProviderMapperImpl implements ProviderMapper {

  @Override
    public  ProviderEntity toEntity(ProviderRegistrationRequest request) {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setName(request.getName());
        providerEntity.setEmail(request.getEmail());
        providerEntity.setPhone(request.getPhone());
        providerEntity.setGovernorate(request.getGovernorate());
        providerEntity.setDelegation(request.getDelegation());
        providerEntity.setAge(request.getAge());
        providerEntity.setServiceCategory(request.getServiceCategory());
        providerEntity.setBasePrice(request.getBasePrice());
        providerEntity.setDescription(request.getDescription());
        providerEntity.setStatus(ProviderStatus.PENDING);
        providerEntity.setRole(Role.PROVIDER);
        providerEntity.setRating(0.0);
        providerEntity.setReviewCount(0);
        providerEntity.setCinName(request.getCin().getOriginalFilename());
        providerEntity.setCvName(request.getCv().getOriginalFilename());
        providerEntity.setDiplomeName(request.getDiplome().getOriginalFilename());

        return providerEntity;
    }

    @Override
    public ProviderSearchResponse toSearchResponse(ProviderEntity entity) {
        ProviderSearchResponse response = new ProviderSearchResponse();
        response.setId(entity.getUserId());
        response.setName(entity.getName());
        response.setServiceCategory(entity.getServiceCategory());
        response.setDelegation(entity.getDelegation());
        response.setBasePrice(entity.getBasePrice());
        response.setRating(entity.getRating());
        response.setReviewCount(entity.getReviewCount());
        response.setDescription(entity.getDescription());
        response.setImageProviderUrl(entity.getProfileImageUrl());
        return response;
    }

    @Override
    public ProviderProfileResponse toProfileResponse(ProviderEntity entity) {
        return ProviderProfileResponse.builder()
                .id(entity.getUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .governorate(entity.getGovernorate())
                .delegation(entity.getDelegation())
                .age(entity.getAge())
                .serviceCategory(entity.getServiceCategory())
                .basePrice(entity.getBasePrice())
                .description(entity.getDescription())
                .profileImageUrl(entity.getProfileImageUrl())
                .build();
    }

}

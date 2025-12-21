package com.servify.provider.service;

import com.servify.provider.dto.ProviderDetailsResponse;
import com.servify.provider.dto.ProviderProfileResponse;
import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderSearchRequest;
import com.servify.provider.dto.ProviderSearchResult;
import com.servify.provider.dto.ProviderSearchResponse;
import com.servify.provider.dto.UpdateProviderProfileRequest;

import com.servify.provider.exceptions.EmailDuplicationException;
import com.servify.provider.mapper.ProviderMapper;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.model.ProviderWorkImage;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;



@Service
@Transactional
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService{

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final PasswordEncoder passwordEncoder ;
    private  final StorageFilesService storageFilesService;



    public ProviderRegistrationResponse register(ProviderRegistrationRequest request) {
        ensureEmailIsAvailable(request.getEmail());
        ProviderEntity savedEntity = providerMapper.toEntity(request);
        savedEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        savedEntity.setCinUrl(storageFilesService.store(request.getCin(),"servify/providers/Cin"));
        savedEntity.setCvUrl(storageFilesService.store(request.getCv(),"servify/providers/Cv"));
        savedEntity.setDiplomeUrl(storageFilesService.store(request.getDiplome(),"servify/providers/Diplome"));

        ProviderEntity saved = providerRepository.save(savedEntity);
        return new ProviderRegistrationResponse(saved.getUserId(), saved.getStatus());
    }

    @Override
    public ProviderSearchResult searchProviders(ProviderSearchRequest request) {
        List<ProviderEntity> providers = providerRepository.searchApprovedProviders(
                request.getServiceCategory(),
                request.getGovernorate(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getMinRating(),
                ProviderStatus.ACCEPTED
        );

        sortProviders(providers, request.getSortBy());

        List<ProviderSearchResponse> results = providers.stream()
                .map(providerMapper::toSearchResponse)
                .toList();

        return new ProviderSearchResult(results, results.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderDetailsResponse getProviderDetails(Long providerId) {
        ProviderEntity provider = providerRepository.findByUserIdAndStatus(providerId, ProviderStatus.ACCEPTED)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        return providerMapper.toDetailsResponse(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderProfileResponse getCurrentProfile() {
        ProviderEntity provider = getCurrentProvider();
        return providerMapper.toProfileResponse(provider);
    }

    @Override
    public ProviderProfileResponse updateProfile(UpdateProviderProfileRequest request) {
        ProviderEntity provider = getCurrentProvider();

        if (request.getName() != null) {
            provider.setName(request.getName());
        }
        if (request.getPhone() != null) {
            provider.setPhone(request.getPhone());
        }
        if (request.getGovernorate() != null) {
            provider.setGovernorate(request.getGovernorate());
        }
        if (request.getDelegation() != null) {
            provider.setDelegation(request.getDelegation());
        }
        if (request.getAge() != null) {
            provider.setAge(request.getAge());
        }
        if (request.getServiceCategory() != null) {
            provider.setServiceCategory(request.getServiceCategory());
        }
        if (request.getBasePrice() != null) {
            provider.setBasePrice(request.getBasePrice());
        }
        if (request.getDescription() != null) {
            provider.setDescription(request.getDescription());
        }

        ProviderEntity saved = providerRepository.save(provider);
        return providerMapper.toProfileResponse(saved);
    }

    @Override
    public ProviderProfileResponse updateProfilePhoto(MultipartFile photo) {
        ProviderEntity provider = getCurrentProvider();
        String imageUrl = storageFilesService.storeImage(photo, "servify/providers/profile");
        provider.setProfileImageUrl(imageUrl);
        ProviderEntity saved = providerRepository.save(provider);
        return providerMapper.toProfileResponse(saved);
    }

    @Override
    public ProviderProfileResponse addWorkImages(List<MultipartFile> images) {
        ProviderEntity provider = getCurrentProvider();
        if (images == null || images.isEmpty()) {
            return providerMapper.toProfileResponse(provider);
        }

        for (MultipartFile image : images) {
            String imageUrl = storageFilesService.storeImage(image, "servify/providers/work");
            ProviderWorkImage workImage = new ProviderWorkImage();
            workImage.setImageUrl(imageUrl);
            workImage.setProvider(provider);
            provider.getWorkImages().add(workImage);
        }

        ProviderEntity saved = providerRepository.save(provider);
        return providerMapper.toProfileResponse(saved);
    }

    private void sortProviders(List<ProviderEntity> providers, String sortBy) {
        if (providers == null || providers.isEmpty()) {
            return;
        }

        if (sortBy == null || sortBy.isBlank()) {
            providers.sort(Comparator.comparing(ProviderEntity::getRating, Comparator.nullsLast(Comparator.reverseOrder())));
            return;
        }

        switch (sortBy) {
            case "PRICE_ASC" -> providers.sort(Comparator.comparing(ProviderEntity::getBasePrice, Comparator.nullsLast(Double::compareTo)));
            case "PRICE_DESC" -> providers.sort(
                    Comparator.comparing(
                                    ProviderEntity::getBasePrice,
                                    // Use nullsFirst then reverse so null prices remain last when sorting desc
                                    Comparator.nullsFirst(Double::compareTo))
                            .reversed()
            );
            case "REVIEWS_DESC" -> providers.sort(Comparator.comparing(ProviderEntity::getReviewCount, Comparator.nullsLast(Integer::compareTo)).reversed());
            default -> providers.sort(Comparator.comparing(ProviderEntity::getRating, Comparator.nullsLast(Double::compareTo)).reversed());
        }
    }

    private void ensureEmailIsAvailable(String email) {
        if (providerRepository.existsByEmail(email)) {
            throw new EmailDuplicationException("Email " + email + " exists");
        }
    }

    private ProviderEntity getCurrentProvider() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return providerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
    }


}

package com.servify.provider.service;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderSearchRequest;
import com.servify.provider.dto.ProviderSearchResult;
import com.servify.provider.dto.ProviderSearchResponse;

import com.servify.provider.exceptions.EmailDuplicationException;
import com.servify.provider.mapper.ProviderMapper;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                request.getDelegation(),
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
            case "PRICE_DESC" -> providers.sort(Comparator.comparing(ProviderEntity::getBasePrice, Comparator.nullsLast(Double::compareTo)).reversed());
            case "REVIEWS_DESC" -> providers.sort(Comparator.comparing(ProviderEntity::getReviewCount, Comparator.nullsLast(Integer::compareTo)).reversed());
            default -> providers.sort(Comparator.comparing(ProviderEntity::getRating, Comparator.nullsLast(Double::compareTo)).reversed());
        }
    }

    private void ensureEmailIsAvailable(String email) {
        if(providerRepository.existsByEmail(email)){
          throw new EmailDuplicationException("Email "+email+" exists" );

    }}


}

package com.servify.provider.service;

import com.servify.provider.dto.ProviderDetailsResponse;
import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderSearchRequest;
import com.servify.provider.dto.ProviderSearchResult;
import com.servify.provider.dto.ProviderProfileResponse;
import com.servify.provider.dto.UpdateProviderProfileRequest;
import com.servify.review.dto.ReviewItemResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProviderService {
  ProviderRegistrationResponse register(ProviderRegistrationRequest request);

  ProviderSearchResult searchProviders(ProviderSearchRequest request);

  ProviderDetailsResponse getProviderDetails(Long providerId);

  ProviderProfileResponse getCurrentProfile();

  ProviderProfileResponse updateProfile(UpdateProviderProfileRequest request);

  ProviderProfileResponse updateProfilePhoto(MultipartFile photo);

  ProviderProfileResponse addWorkImages(List<MultipartFile> images);

  List<ReviewItemResponse> getProviderReviews(Long providerId);
}

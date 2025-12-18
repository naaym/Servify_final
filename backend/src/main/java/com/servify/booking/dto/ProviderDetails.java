package com.servify.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderDetails {
    private Long providerId;
    private String providerLocalisation;
    private String providerName;
    private String providerPhone;
    private FileMetadata providerImage;
    private Double providerRate;
    private String profileImageUrl;
}

package com.servify.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProviderRevenueSummaryResponse {
    private Long providerId;
    private String providerName;
    private Long grossAmount;
    private Long providerNetAmount;
    private Long platformFeeAmount;
    private String currency;
}

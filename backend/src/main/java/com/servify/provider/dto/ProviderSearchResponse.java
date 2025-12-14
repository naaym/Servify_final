package com.servify.provider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderSearchResponse {
    private Long id;
    private String name;
    private String serviceCategory;
    private String delegation;
    private Double basePrice;
    private Double rating;
    private Integer reviewCount;
    private String description;
}

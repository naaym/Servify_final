package com.servify.provider.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProviderSearchRequest {
    private String serviceCategory;
    private String governorate;
    private Double minPrice;
    private Double maxPrice;
    private Double minRating;
    private String sortBy;
}

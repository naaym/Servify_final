package com.servify.provider.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProviderDetailsResponse {
    private Long id;
    private String name;
    private String serviceCategory;
    private String governorate;
    private String delegation;
    private Double basePrice;
    private Double rating;
    private Integer reviewCount;
    private String description;
    private String imageProviderUrl;
    private List<String> workImages;
}

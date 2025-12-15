package com.servify.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProviderSearchResult {
    private List<ProviderSearchResponse> providers;
    private int total;
}

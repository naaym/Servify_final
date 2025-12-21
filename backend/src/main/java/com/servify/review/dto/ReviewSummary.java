package com.servify.review.dto;

public record ReviewSummary(
        Double politenessAverage,
        Double qualityAverage,
        Double punctualityAverage,
        Double overallAverage,
        Long reviewCount
) {
}

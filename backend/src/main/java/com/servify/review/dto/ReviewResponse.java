package com.servify.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private Long bookingId;
    private Double overallRating;
    private Integer politenessRating;
    private Integer qualityRating;
    private Integer punctualityRating;
    private String comment;
    private Long createdAt;
}

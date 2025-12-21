package com.servify.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewItemResponse {
    private Long id;
    private String clientName;
    private String clientProfileImageUrl;
    private double overallRating;
    private Integer politenessRating;
    private Integer qualityRating;
    private Integer punctualityRating;
    private String comment;
    private Long createdAt;
}

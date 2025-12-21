package com.servify.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer politenessRating;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer qualityRating;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer punctualityRating;

    private String comment;
}

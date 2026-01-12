package org.example.librex.database.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReviewRequest {

    @NotNull
    private Integer titleId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer stars;

    @Size(max = 500)
    private String content; // opcjonalne

    public Integer getTitleId() { return titleId; }
    public Integer getStars() { return stars; }
    public String getContent() { return content; }
}


package org.example.librex.database.review.dto;

import org.example.librex.database.review.Review;

import java.time.LocalDateTime;

public class ReviewResponse {

    private final Integer id;
    private final String bookTitle;
    private final Integer stars;
    private final String content;
    private final LocalDateTime createdAt;

    //TODO: UserID aby admin widział kto wystawił opinię

    public ReviewResponse(Integer id, String bookTitle, Integer stars, String content, LocalDateTime createdAt) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.stars = stars;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static ReviewResponse from(Review r, String bookTitle) {
        return new ReviewResponse(
                r.getId(),
                bookTitle,
                r.getStars(),
                r.getContent(),
                r.getCreateDate()
        );
    }

    public static ReviewResponse from(Review r) {
        return from(r, null);
    }

    public Integer getId() {
        return id;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Integer getStars() {
        return stars;
    }
    public String getContent() {
        return content;
    }
}

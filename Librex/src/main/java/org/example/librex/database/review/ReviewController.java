package org.example.librex.database.review;

import jakarta.validation.Valid;
import org.example.librex.database.review.dto.CreateReviewRequest;
import org.example.librex.database.review.dto.ReviewResponse;
import org.example.librex.database.users.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final AppUserService appUserService;

    public ReviewController(ReviewService reviewService,  AppUserService appUserService) {
        this.reviewService = reviewService;
        this.appUserService = appUserService;
    }

    private Integer getCurrentUserId(Authentication auth) {
        return appUserService.findByUsername(auth.getName()).getId();
    }

    @PostMapping
    public ResponseEntity<Void> createReview(@Valid @RequestBody CreateReviewRequest req, Authentication auth) {
        Integer userId = getCurrentUserId(auth);

        reviewService.createReview(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //TODO: wyswietlenie wszystkich recenzji

    @GetMapping("/title/{titleId}")
    public List<ReviewResponse> getBookReviews(@PathVariable Integer titleId) {
        return reviewService.getBookReviews(titleId);
    }

    @GetMapping("/user")
    public List<ReviewResponse> getUserReviews(Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        return reviewService.getUserReviews(userId);

    }

    @GetMapping("/user/title/{titleId}")
    public List<ReviewResponse> getUserReviews(@PathVariable Integer titleId, Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        return reviewService.getUserReviews(userId, titleId);
    }

}

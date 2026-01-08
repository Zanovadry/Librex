package org.example.librex.database.review;

import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.books.title.BookTitleRepository;
import org.example.librex.database.review.dto.CreateReviewRequest;
import org.example.librex.database.review.dto.ReviewResponse;
import org.example.librex.database.users.AppUser;
import org.example.librex.database.users.AppUserRepository;
import org.example.librex.database.users.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final AppUserRepository appUserRepository;
    private final BookTitleRepository bookTitleRepository;
    private final ReviewRepository repository;

    public ReviewService(AppUserRepository appUserRepository,  BookTitleRepository bookTitleRepository, ReviewRepository repository) {
        this.appUserRepository = appUserRepository;
        this.bookTitleRepository = bookTitleRepository;
        this.repository = repository;
    }

    @Transactional
    public void createReview(Integer userId, CreateReviewRequest req) {
        AppUser user = appUserRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        BookTitle title = bookTitleRepository.findById(req.getTitleId()).orElseThrow(NoSuchElementException::new);

        Integer stars = req.getStars();
        String content = req.getContent();

        Review review = new Review(
                user,
                title,
                stars,
                (content == null || content.isBlank()) ? null : content,
                LocalDateTime.now()
        );

        repository.save(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getBookReviews(Integer bookId) {

        bookTitleRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book Not Found"));

        List<Review> reviews = repository.findByTitle_IdOrderByCreateDate(bookId);

        return reviews.stream()
                .map(ReviewResponse::from)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserReviews(Integer userId) {
        List<Review> reviews = repository.findByUser_IdOrderByCreateDate(userId);

        Set<Integer> titleIds = reviews.stream()
                .map(r -> r.getTitle().getId())
                .collect(Collectors.toSet());

        Map<Integer, String> titlesById = bookTitleRepository.findAllById(titleIds).stream()
                .collect(Collectors.toMap(
                        BookTitle::getId,
                        BookTitle::getTitle
                ));

        return reviews.stream()
                .map(r -> ReviewResponse.from(r, titlesById.get(r.getTitle().getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserReviews(Integer userId, Integer titleId) {
        String title = bookTitleRepository.findById(titleId)
                .map(BookTitle::getTitle)
                .orElseThrow(() -> new IllegalArgumentException("BookTitle not found: " + titleId));

        return repository.findByUser_IdAndTitle_IdOrderByCreateDate(userId, titleId).stream()
                .map(r -> ReviewResponse.from(r, title))
                .toList();
    }
}

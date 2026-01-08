package org.example.librex.database.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByUser_IdOrderByCreateDate(Integer userId);

    List<Review> findByTitle_IdOrderByCreateDate(Integer titleId);

    List<Review> findByUser_IdAndTitle_IdOrderByCreateDate(Integer userId, Integer titleId);

}

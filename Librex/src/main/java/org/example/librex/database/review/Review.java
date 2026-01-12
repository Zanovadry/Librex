package org.example.librex.database.review;

import jakarta.persistence.*;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.users.AppUser;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title_id", nullable = false)
    private BookTitle title;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    public Review() {}

    public Review(AppUser user, BookTitle title, Integer stars, String content, LocalDateTime createDate) {
        this.user = user;
        this.title = title;
        this.stars = stars;
        this.content = content;
        this.createDate = createDate;
    }

    public AppUser getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

    public BookTitle getTitle() {
        return title;
    }

    public Integer getStars() {
        return stars;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}

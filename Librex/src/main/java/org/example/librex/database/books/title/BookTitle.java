package org.example.librex.database.books.title;

import jakarta.persistence.*;
import org.example.librex.database.author.Author;

@Entity
@Table(name = "book_title")
public class BookTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_id")
    private Integer id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "description")
    private String description;

    @Column(name = "photo", length = 500)
    private String photo;

    protected BookTitle() {
    }

    public BookTitle(String title,
                     Author author,
                     String description,
                     String photo) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}


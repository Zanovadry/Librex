package org.example.librex.database.books.title.dto;

public class BookResponse {

    private Integer id;
    private String title;
    private String author;
    private String description;

    // pod kafelki – opcjonalne
    private String photo;
    private String language;
    private String category;
    private long availableCopies;

    public BookResponse(Integer id, String title, String author, String description, String photo, String language, String category, long availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.photo = photo;
        this.language = language;
        this.category = category;
        this.availableCopies = availableCopies;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getLanguage() {
        return language;
    }

    public long getAvailableCopies() {
        return availableCopies;
    }
}

package org.example.librex.database.books.title.dto;

public class BookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String description;

    // pod kafelki – opcjonalne
    private String photo;
    private String language;
    private String category;

    public BookResponse(Integer id,
                        String title,
                        String authorName,
                        String description,
                        String photo,
                        String language,
                        String category) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.description = description;
        this.photo = photo;
        this.language = language;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
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

    public String getCategory() {
        return category;
    }
}

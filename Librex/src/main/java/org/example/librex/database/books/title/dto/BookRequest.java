package org.example.librex.database.books.title.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotNull
    private Integer authorId;

    @Size(max = 2000)
    private String description;

    @Size(max = 500)
    private String photo;

    // na przyszłość można dodać listę categoryIds itd.

    public String getTitle() {
        return title;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }
}

package org.example.librex.database.books.category;

import java.io.Serializable;
import java.util.Objects;

public class BookCategoryId implements Serializable {

    private Integer category;
    private Integer title;

    public BookCategoryId() {
    }

    public BookCategoryId(Integer category, Integer title) {
        this.category = category;
        this.title = title;
    }

    public Integer getCategory() {
        return category;
    }

    public Integer getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookCategoryId)) return false;
        BookCategoryId that = (BookCategoryId) o;
        return Objects.equals(category, that.category)
                && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, title);
    }
}


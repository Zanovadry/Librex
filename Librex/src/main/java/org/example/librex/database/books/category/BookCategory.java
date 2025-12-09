package org.example.librex.database.books.category;

import jakarta.persistence.*;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.dictionaries.category.Category;

@Entity
@Table(name = "book_category")
@IdClass(BookCategoryId.class)
public class BookCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title_id", nullable = false)
    private BookTitle title;

    protected BookCategory() {
    }

    public BookCategory(Category category, BookTitle title) {
        this.category = category;
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public BookTitle getTitle() {
        return title;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTitle(BookTitle title) {
        this.title = title;
    }
}

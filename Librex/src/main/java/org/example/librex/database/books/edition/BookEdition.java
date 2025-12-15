package org.example.librex.database.books.edition;

import jakarta.persistence.*;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.dictionaries.language.Language;
import org.example.librex.database.publisher.Publisher;


import java.math.BigDecimal;

@Entity
@Table(name = "book_edition")
public class BookEdition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edition_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title_id", nullable = false)
    private BookTitle title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "isbn", length = 25)
    private String isbn;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(name = "is_hard_cover", nullable = false)
    private boolean hardCover;

    @Column(name = "value", precision = 19, scale = 2)
    private BigDecimal value;

    @Lob
    @Column(name = "notes")
    private String notes;

    protected BookEdition() {
    }

    public BookEdition(BookTitle title,
                       Publisher publisher,
                       Language language,
                       String isbn,
                       Integer pages,
                       Integer publishYear,
                       boolean hardCover,
                       BigDecimal value,
                       String notes) {
        this.title = title;
        this.publisher = publisher;
        this.language = language;
        this.isbn = isbn;
        this.pages = pages;
        this.publishYear = publishYear;
        this.hardCover = hardCover;
        this.value = value;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public BookTitle getTitle() {
        return title;
    }

    public void setTitle(BookTitle title) {
        this.title = title;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public boolean isHardCover() {
        return hardCover;
    }

    public void setHardCover(boolean hardCover) {
        this.hardCover = hardCover;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}


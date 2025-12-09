package org.example.librex.database.books.copy;

import jakarta.persistence.*;
import org.example.librex.database.books.edition.BookEdition;

@Entity
@Table(name = "book_copy")
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_id")
    private Integer id;

    //TODO: ???
    @Column(name = "inventory_number", length = 50, nullable = false, unique = true)
    private String inventoryNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "edition_id", nullable = false)
    private BookEdition edition;

    @Column(name = "condition", length = 100)
    private String condition;

    @Lob
    @Column(name = "defects")
    private String defects;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    protected BookCopy() {
    }

    public BookCopy(String inventoryNumber,
                    BookEdition edition,
                    String condition,
                    String defects,
                    boolean available) {
        this.inventoryNumber = inventoryNumber;
        this.edition = edition;
        this.condition = condition;
        this.defects = defects;
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public BookEdition getEdition() {
        return edition;
    }

    public void setEdition(BookEdition edition) {
        this.edition = edition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDefects() {
        return defects;
    }

    public void setDefects(String defects) {
        this.defects = defects;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

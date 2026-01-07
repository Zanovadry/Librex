package org.example.librex.database.waitlist;

import jakarta.persistence.*;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.users.AppUser;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "waitlist",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "title_id"}),
                @UniqueConstraint(columnNames = {"title_id", "position"})
        }
)
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waitlist_id")
    private Integer waitlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", nullable = false)
    private BookTitle bookTitle;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "position")
    private Integer position;

    protected Waitlist() {
    }

    public Waitlist(Integer position,
                    LocalDateTime createDate,
                    boolean active,
                    BookTitle bookTitle,
                    AppUser appUser) {
        this.position = position;
        this.createDate = createDate;
        this.active = active;
        this.bookTitle = bookTitle;
        this.appUser = appUser;
    }

    public Integer getWaitlistId() {
        return waitlistId;
    }

    public Integer getPosition() {
        return position;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public Boolean getActive() {
        return active;
    }

    public BookTitle getBookTitle() {
        return bookTitle;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    //TODO: position in waitlist never changes, fix this
    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}


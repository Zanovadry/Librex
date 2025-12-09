package org.example.librex.database.waitlist;

import jakarta.persistence.*;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.users.AppUser;

import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist")
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WaitlistID")
    private Integer waitlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "UserID", nullable = false)
    private AppUser appUser;   // albo @ManyToOne do encji User

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "TitleID", nullable = false)
    private BookTitle bookTitle;  // albo @ManyToOne do encji Title

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreateDate", nullable = false)
    private LocalDateTime createDate;

    //TODO: position?
    @Column(name = "Position")
    private Integer position;


    protected Waitlist() {
    }

    public Waitlist(Integer position, LocalDateTime createDate, Boolean isActive, BookTitle bookTitle, AppUser appUser) {
        this.position = position;
        this.createDate = createDate;
        this.isActive = isActive;
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
        return isActive;
    }

    public BookTitle getBookTitle() {
        return bookTitle;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}


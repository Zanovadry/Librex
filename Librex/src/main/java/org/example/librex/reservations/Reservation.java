package org.example.librex.reservations;

import jakarta.persistence.*;
import org.example.librex.users.AppUser;
import org.example.librex.books.bookcopy.BookCopy;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_id", nullable = false)
    private BookCopy copy;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @Column(name = "is_damaged", nullable = false)
    private boolean damaged = false;

    @Column(name = "damage_details", length = 500)
    private String damageDetails;

    protected Reservation() {
    }

    public Reservation(AppUser user,
                       BookCopy copy,
                       LocalDateTime createDate,
                       LocalDate expectedReturnDate,
                       LocalDate returnDate,
                       boolean damaged,
                       String damageDetails) {
        this.user = user;
        this.copy = copy;
        this.createDate = createDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
        this.damaged = damaged;
        this.damageDetails = damageDetails;
    }

    public Integer getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public BookCopy getCopy() {
        return copy;
    }

    public void setCopy(BookCopy copy) {
        this.copy = copy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public String getDamageDetails() {
        return damageDetails;
    }

    public void setDamageDetails(String damageDetails) {
        this.damageDetails = damageDetails;
    }
}

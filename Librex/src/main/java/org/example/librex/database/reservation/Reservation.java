package org.example.librex.database.reservation;

import jakarta.persistence.*;
import org.example.librex.database.books.copy.BookCopy;
import org.example.librex.database.users.AppUser;

import java.time.LocalDate;

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
    private LocalDate createDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @Column(name = "damage_details", length = 500)
    private String damageDetails;

    protected Reservation() {
    }

    //TODO: front może nie działać bo usunięte zostało isDamaged
    public Reservation(AppUser user,
                       BookCopy copy,
                       LocalDate createDate,
                       LocalDate expectedReturnDate,
                       LocalDate returnDate,
                       String damageDetails) {
        this.user = user;
        this.copy = copy;
        this.createDate = createDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
        this.damageDetails = damageDetails;
    }

    public boolean isDamaged() {
        return damageDetails != null;
    }

    public boolean isReturnedLate() {
        if (returnDate == null) {
            return false;
        } else {
            return returnDate.isAfter(expectedReturnDate);
        }
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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
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

    public String getDamageDetails() {
        return damageDetails;
    }

    public void setDamageDetails(String damageDetails) {
        this.damageDetails = damageDetails;
    }
}

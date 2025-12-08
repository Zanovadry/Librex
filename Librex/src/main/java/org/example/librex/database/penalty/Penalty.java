package org.example.librex.database.penalty;

import jakarta.persistence.*;
import org.example.librex.database.reservation.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "penalty")
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "days_late", nullable = false)
    private Integer daysLate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_paid", nullable = false)
    private boolean paid = false;

    //TODO: do wee need this?
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    protected Penalty() {
    }

    public Penalty(Reservation reservation,
                   BigDecimal amount,
                   Integer daysLate,
                   LocalDateTime createdAt,
                   boolean paid,
                   LocalDateTime paidAt) {
        this.reservation = reservation;
        this.amount = amount;
        this.daysLate = daysLate;
        this.createdAt = createdAt;
        this.paid = paid;
        this.paidAt = paidAt;
    }

    public Integer getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(Integer daysLate) {
        this.daysLate = daysLate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}


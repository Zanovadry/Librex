package org.example.librex.database.users.dto;

import java.time.LocalDate;

public class UserReservationDto {
    private Integer reservationId;
    private Integer copyId;
    private String bookTitle;
    private String inventoryNumber;
    private LocalDate expectedReturnDate;
    private boolean overdue;

    public UserReservationDto(Integer reservationId, Integer copyId, String bookTitle, String inventoryNumber, LocalDate expectedReturnDate) {
        this.reservationId = reservationId;
        this.copyId = copyId;
        this.bookTitle = bookTitle;
        this.inventoryNumber = inventoryNumber;
        this.expectedReturnDate = expectedReturnDate;
        this.overdue = LocalDate.now().isAfter(expectedReturnDate);
    }

    // Getters
    public Integer getReservationId() { return reservationId; }
    public Integer getCopyId() { return copyId; }
    public String getBookTitle() { return bookTitle; }
    public String getInventoryNumber() { return inventoryNumber; }
    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public boolean isOverdue() { return overdue; }
}

package org.example.librex.database.reservation.dto;

import java.time.LocalDate;

public class ReservationResponse {

    private Integer id;
    private String userEmail;
    private String bookTitle;
    private String inventoryNumber;
    private LocalDate createDate;
    private LocalDate expectedReturnDate;
    private LocalDate returnDate;
    private boolean active;

    public ReservationResponse(Integer id, String userEmail, String bookTitle, String inventoryNumber, LocalDate createDate, LocalDate expectedReturnDate, LocalDate returnDate) {
        this.id = id;
        this.userEmail = userEmail;
        this.bookTitle = bookTitle;
        this.inventoryNumber = inventoryNumber;
        this.createDate = createDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
        this.active = (returnDate == null);
    }

    // Getters
    public Integer getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public String getBookTitle() { return bookTitle; }
    public String getInventoryNumber() { return inventoryNumber; }
    public LocalDate getCreateDate() { return createDate; }
    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isActive() { return active; }
}

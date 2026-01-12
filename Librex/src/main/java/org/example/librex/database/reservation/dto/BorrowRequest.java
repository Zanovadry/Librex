package org.example.librex.database.reservation.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer copyId;
    
    // Opcjonalnie: ile dni na wypożyczenie (domyślnie np. 14)
    private Integer days;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCopyId() {
        return copyId;
    }

    public void setCopyId(Integer copyId) {
        this.copyId = copyId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}

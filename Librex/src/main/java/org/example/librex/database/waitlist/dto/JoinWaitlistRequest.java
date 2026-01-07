package org.example.librex.database.waitlist.dto;

import jakarta.validation.constraints.NotNull;

public class JoinWaitlistRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer bookTitleId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookTitleId() {
        return bookTitleId;
    }

    public void setBookTitleId(Integer bookTitleId) {
        this.bookTitleId = bookTitleId;
    }
}

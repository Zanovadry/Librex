package org.example.librex.database.users.dto;

import java.time.LocalDateTime;

public class UserWaitlistDto {
    private Integer waitlistId;
    private String bookTitle;
    private Integer position;
    private LocalDateTime joinDate;

    public UserWaitlistDto(Integer waitlistId, String bookTitle, Integer position, LocalDateTime joinDate) {
        this.waitlistId = waitlistId;
        this.bookTitle = bookTitle;
        this.position = position;
        this.joinDate = joinDate;
    }

    // Getters
    public Integer getWaitlistId() { return waitlistId; }
    public String getBookTitle() { return bookTitle; }
    public Integer getPosition() { return position; }
    public LocalDateTime getJoinDate() { return joinDate; }
}

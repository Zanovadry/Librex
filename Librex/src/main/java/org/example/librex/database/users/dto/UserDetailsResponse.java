package org.example.librex.database.users.dto;

import java.util.List;

public class UserDetailsResponse {
    private UserResponse user;
    private List<UserReservationDto> activeReservations;
    private List<UserWaitlistDto> waitlistItems;

    public UserDetailsResponse(UserResponse user, List<UserReservationDto> activeReservations, List<UserWaitlistDto> waitlistItems) {
        this.user = user;
        this.activeReservations = activeReservations;
        this.waitlistItems = waitlistItems;
    }

    // Getters
    public UserResponse getUser() { return user; }
    public List<UserReservationDto> getActiveReservations() { return activeReservations; }
    public List<UserWaitlistDto> getWaitlistItems() { return waitlistItems; }
}

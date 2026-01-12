package org.example.librex.database.users;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        String message = "User not found";
        super(message);
    }
}

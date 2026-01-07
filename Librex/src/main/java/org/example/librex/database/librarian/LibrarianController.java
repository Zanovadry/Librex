package org.example.librex.database.librarian;

import org.example.librex.database.users.AppUserService;
import org.example.librex.database.users.dto.UserDetailsResponse;
import org.example.librex.database.users.dto.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/librarian")
@PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
public class LibrarianController {

    private final AppUserService userService;

    public LibrarianController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/search")
    public List<UserResponse> searchUsers(@RequestParam String query) {
        return userService.searchUsers(query);
    }

    @GetMapping("/users/{userId}")
    public UserDetailsResponse getUserDetails(@PathVariable Integer userId) {
        return userService.getUserDetails(userId);
    }
}

package org.example.librex.users;

import org.example.librex.users.dto.RegistrationRequest;
import org.example.librex.users.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService userService;

    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Integer id,
                                   @RequestBody RegistrationRequest update) {
        return userService.updateUser(id, update);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

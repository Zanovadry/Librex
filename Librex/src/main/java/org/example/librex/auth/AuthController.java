package org.example.librex.auth;

import jakarta.validation.Valid;
import org.example.librex.database.users.AppUserService;
import org.example.librex.database.users.dto.RegistrationRequest;
import org.example.librex.database.users.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserService userService;

    public AuthController(AppUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegistrationRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public org.example.librex.database.users.dto.UserDetailsResponse me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = authentication.getName();
        org.example.librex.database.users.dto.UserResponse user = userService.findByUsername(username);
        return userService.getUserDetails(user.getId());
    }
}

package org.example.librex.auth;

import jakarta.validation.Valid;
import org.example.librex.users.AppUserService;
import org.example.librex.users.dto.RegistrationRequest;
import org.example.librex.users.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

package org.example.librex.database.waitlist;

import jakarta.validation.Valid;
import org.example.librex.database.waitlist.dto.JoinWaitlistRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {

    private final WaitlistService waitlistService;

    public WaitlistController(WaitlistService waitlistService) {
        this.waitlistService = waitlistService;
    }

    @PostMapping("/join")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN') or hasRole('CUSTOMER')")
    public ResponseEntity<String> joinWaitlist(@Valid @RequestBody JoinWaitlistRequest request) {
        waitlistService.joinWaitlist(request);
        return ResponseEntity.ok("Successfully added to waitlist.");
    }
}

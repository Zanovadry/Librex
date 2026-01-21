package org.example.librex.database.reservation;

import jakarta.validation.Valid;
import org.example.librex.database.reservation.dto.BorrowRequest;
import org.example.librex.database.reservation.dto.ProlongRequest;
import org.example.librex.database.reservation.dto.ReservationResponse;
import org.example.librex.database.reservation.dto.ReturnRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN') or hasRole('CUSTOMER')")
    @PostMapping("/borrow")
    public ReservationResponse borrowBook(@Valid @RequestBody BorrowRequest request) {
        return reservationService.borrowBook(request);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@Valid @RequestBody ReturnRequest request) {
        String message = reservationService.returnBook(request);
        return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @PostMapping("/prolong")
    public ReservationResponse prolongReservation(@Valid @RequestBody ProlongRequest request) {
        return reservationService.prolongReservation(request);
    }
}

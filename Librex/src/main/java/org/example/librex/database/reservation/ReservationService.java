package org.example.librex.database.reservation;

import org.example.librex.database.books.copy.BookCopy;
import org.example.librex.database.books.copy.BookCopyRepository;
import org.example.librex.database.notification.NotificationService;
import org.example.librex.database.penalty.Penalty;
import org.example.librex.database.penalty.PenaltyRepository;
import org.example.librex.database.reservation.dto.BorrowRequest;
import org.example.librex.database.reservation.dto.ProlongRequest;
import org.example.librex.database.reservation.dto.ReservationResponse;
import org.example.librex.database.reservation.dto.ReturnRequest;
import org.example.librex.database.users.AppUser;
import org.example.librex.database.users.AppUserRepository;
import org.example.librex.database.waitlist.Waitlist;
import org.example.librex.database.waitlist.WaitlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookCopyRepository bookCopyRepository;
    private final AppUserRepository appUserRepository;
    private final PenaltyRepository penaltyRepository;
    private final WaitlistRepository waitlistRepository;
    private final NotificationService notificationService;

    private static final BigDecimal DAILY_LATE_FEE = new BigDecimal("0.50"); // 50 groszy za dzień
    private static final Integer MAX_PROLONG_DAYS_AT_ONCE = 14;

    public ReservationService(ReservationRepository reservationRepository,
                              BookCopyRepository bookCopyRepository,
                              AppUserRepository appUserRepository,
                              PenaltyRepository penaltyRepository,
                              WaitlistRepository waitlistRepository, NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.appUserRepository = appUserRepository;
        this.penaltyRepository = penaltyRepository;
        this.waitlistRepository = waitlistRepository;
        this.notificationService = notificationService;
    }


    @Transactional
    public ReservationResponse borrowBook(BorrowRequest request) {

        AppUser user = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        List<Penalty> unpaidPenalties = penaltyRepository.findByReservation_User_IdAndPaidFalse(user.getId());
        if (!unpaidPenalties.isEmpty()) {
            throw new IllegalStateException("User has unpaid penalties. Cannot borrow books.");
        }


        BookCopy copy = bookCopyRepository.findById(request.getCopyId())
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));


        if (!copy.isAvailable()) {
            throw new IllegalStateException("Book copy is not available (already borrowed).");
        }


        int days = (request.getDays() != null && request.getDays() > 0) ? request.getDays() : 14;
        LocalDate now = LocalDate.now();
        LocalDate expectedReturn = LocalDate.now().plusDays(days);

        Reservation reservation = new Reservation(
                user,
                copy,
                now,
                expectedReturn,
                null,
                null
        );


        copy.setAvailable(false);
        bookCopyRepository.save(copy);
        
        Reservation saved = reservationRepository.save(reservation);



        return toResponse(saved);
    }


    @Transactional
    public String returnBook(ReturnRequest request) {

        Reservation reservation = reservationRepository.findByCopy_IdAndReturnDateIsNull(request.getCopyId())
                .orElseThrow(() -> new IllegalArgumentException("No active reservation found for this book copy."));


        LocalDate today = LocalDate.now();
        reservation.setReturnDate(today);


        reservation.setDamageDetails(request.getDamageDetails());


        BigDecimal totalPenalty = BigDecimal.ZERO;


        long daysLate = ChronoUnit.DAYS.between(reservation.getExpectedReturnDate(), today);
        if (daysLate > 0) {
            BigDecimal lateFee = DAILY_LATE_FEE.multiply(BigDecimal.valueOf(daysLate));
            totalPenalty = totalPenalty.add(lateFee);
        }


        if (request.getDamageFee() != null && request.getDamageFee().compareTo(BigDecimal.ZERO) > 0) {
            totalPenalty = totalPenalty.add(request.getDamageFee());
        }


        if (totalPenalty.compareTo(BigDecimal.ZERO) > 0) {
            Penalty penalty = new Penalty(
                    reservation,
                    totalPenalty,
                    daysLate > 0 ? (int) daysLate : 0,
                    LocalDateTime.now(),
                    false,
                    null
            );
            penaltyRepository.save(penalty);
        }

        reservationRepository.save(reservation);


        BookCopy copy = reservation.getCopy();
        copy.setAvailable(true);
        
        if (request.getDamageDetails() != null) {

            String oldCondition = copy.getCondition() != null ? copy.getCondition() : "";
            copy.setCondition(oldCondition + " (Damaged: " + request.getDamageDetails() + ")");
        }
        
        bookCopyRepository.save(copy);


        Integer titleId = copy.getEdition().getTitle().getId();
        List<Waitlist> queue = waitlistRepository.findByBookTitle_IdAndActiveTrueOrderByPositionAsc(titleId);

        StringBuilder message = new StringBuilder("Book returned successfully.");
        if (totalPenalty.compareTo(BigDecimal.ZERO) > 0) {
            message.append(" Penalty charged: ").append(totalPenalty).append(" PLN.");
        }

        if (!queue.isEmpty()) {
            Waitlist nextPerson = queue.getFirst();
            message.append(" ATTENTION: User ").append(nextPerson.getAppUser().getEmail())
                   .append(" is waiting for this title (Position 1).");

            notificationService.createBookAvailableNotification(
                    nextPerson.getAppUser().getId(), nextPerson.getBookTitle().getTitle()
            );

            nextPerson.setActive(false);
            waitlistRepository.save(nextPerson);
        }

        return message.toString();
    }

    @Transactional
    public ReservationResponse prolongReservation(ProlongRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getReturnDate() != null) {
            throw new IllegalStateException("Cannot prolong a returned reservation.");
        }

        Integer titleId = reservation.getCopy().getEdition().getTitle().getId();
        List<Waitlist> queue = waitlistRepository.findByBookTitle_IdAndActiveTrueOrderByPositionAsc(titleId);
        if (!queue.isEmpty()) {
            throw new IllegalStateException("Cannot prolong. There are users waiting for this title.");
        }

        Integer userId = reservation.getUser().getId();
        List<Penalty> unpaidPenalties = penaltyRepository.findByReservation_User_IdAndPaidFalse(userId);
        if (!unpaidPenalties.isEmpty()) {
            throw new IllegalStateException("User has unpaid penalties. Cannot prolong reservation.");
        }

        int extendDays = request.getExtendDays();


        if (extendDays > MAX_PROLONG_DAYS_AT_ONCE) {
            throw new IllegalStateException("extendDays is too large.");
        }

        LocalDate currentExpected = reservation.getExpectedReturnDate();
        LocalDate today = LocalDate.now();

        if (currentExpected.isBefore(today)) {
            throw new IllegalStateException("Cannot prolong after due date has passed. Return the book first.");
        }

        reservation.setExpectedReturnDate(currentExpected.plusDays(extendDays));

        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved);
    }

    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getUser().getEmail(),
                r.getCopy().getEdition().getTitle().getTitle(),
                r.getCopy().getInventoryNumber(),
                r.getCreateDate(),
                r.getExpectedReturnDate(),
                r.getReturnDate()
        );
    }
}

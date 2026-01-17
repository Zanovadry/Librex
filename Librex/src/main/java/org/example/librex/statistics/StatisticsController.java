package org.example.librex.statistics;

import org.example.librex.statistics.dto.reservation.ReservationStatisticsRequest;
import org.example.librex.statistics.dto.reservation.ReservationStatisticsResponse;
import org.example.librex.statistics.dto.user.UserStatisticsResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/users")
    public ResponseEntity<UserStatisticsResponse> getUsersStatistics() {
        return ResponseEntity.ok(statisticsService.getUsersStatistics());
    }

    @GetMapping("/reservations")
    public ResponseEntity<ReservationStatisticsResponse> getReservationsStatistics(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        LocalDate from = fromDate != null ? fromDate : LocalDate.of(1970, 1, 1);
        LocalDate to = toDate != null ? toDate : LocalDate.now();

        if (to.isBefore(from)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                statisticsService.getReservationsStatisticsByDate(from, to)
        );
    }

}

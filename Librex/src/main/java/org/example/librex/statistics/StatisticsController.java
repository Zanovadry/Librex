package org.example.librex.statistics;

import org.example.librex.statistics.dto.ReservationStatisticsRequest;
import org.example.librex.statistics.dto.ReservationStatisticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<ReservationStatisticsResponse> getReservationStatistics(ReservationStatisticsRequest request) {

        LocalDate from = request.fromDate();
        LocalDate to = request.toDate();

        if (to.isBefore(from)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(statisticsService.getReservationStatisticsByDate(from, to));
    }
}

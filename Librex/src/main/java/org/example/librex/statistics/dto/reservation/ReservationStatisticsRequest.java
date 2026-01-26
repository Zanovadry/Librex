package org.example.librex.statistics.dto.reservation;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ReservationStatisticsRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
) {}
